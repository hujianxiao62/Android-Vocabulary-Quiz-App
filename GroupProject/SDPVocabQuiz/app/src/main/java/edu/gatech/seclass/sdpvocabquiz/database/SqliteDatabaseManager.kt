package edu.gatech.seclass.sdpvocabquiz.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import edu.gatech.seclass.sdpvocabquiz.adt.Quiz
import edu.gatech.seclass.sdpvocabquiz.adt.Score
import edu.gatech.seclass.sdpvocabquiz.adt.Student
import edu.gatech.seclass.sdpvocabquiz.adt.Word
import android.util.Base64
import edu.gatech.seclass.sdpvocabquiz.QuizMaster
import java.util.logging.Logger

class SqliteDatabaseManager : DatabaseManager, SQLiteOpenHelper {

    private var context: Context? = null
    val log = Logger.getLogger(SqliteDatabaseManager::class.java.name)
    override val createTableStatements = mapOf(
            QuizMaster.Constants.wordTable to "CREATE TABLE IF NOT EXISTS ${QuizMaster.Constants.wordTable} (value TEXT NOT NULL, definition TEXT NOT NULL, quiz TEXT NOT NULL, PRIMARY KEY(value, quiz))",
            QuizMaster.Constants.definitionTable to "CREATE TABLE IF NOT EXISTS ${QuizMaster.Constants.definitionTable} (value TEXT NOT NULL, quiz TEXT NOT NULL, PRIMARY KEY(value, quiz))",
            QuizMaster.Constants.quizTable to "CREATE TABLE IF NOT EXISTS ${QuizMaster.Constants.quizTable} (owner TEXT NOT NULL, name TEXT PRIMARY KEY, description TEXT NOT NULL)",
            QuizMaster.Constants.studentTable to "CREATE TABLE IF NOT EXISTS ${QuizMaster.Constants.studentTable} (username TEXT PRIMARY KEY, major TEXT NOT NULL, senioritylevel TEXT NOT NULL, emailaddress TEXT NOT NULL)",
            QuizMaster.Constants.scoreTable to "CREATE TABLE IF NOT EXISTS ${QuizMaster.Constants.scoreTable} (student TEXT NOT NULL, quiz TEXT NOT NULL, percentage DOUBLE, ts BIGINT, PRIMARY KEY(student, quiz, ts))"
    )
    val sqliteNow = "CAST((julianday('now') - 2440587.5)*86400000 AS INTEGER)"

    constructor(context: Context): super(context, QuizMaster.Constants.dbName, null, QuizMaster.Constants.dbVersion) {
        this.context = context
    }

    override fun onCreate(db: SQLiteDatabase?) {
        createTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        dropTables(db)
        createTables(db)
    }

    override fun dropTables() { dropTables(writableDatabase) }
    internal fun dropTables(db: SQLiteDatabase?) {
        QuizMaster.Constants.tables.forEach { table -> db!!.execSQL("DROP TABLE IF EXISTS " + table) }
    }

    override fun createTables() { createTables(writableDatabase) }
    internal fun createTables(db: SQLiteDatabase?) {
        QuizMaster.Constants.tables.forEach { table -> db!!.execSQL(createTableStatements[table]) }
    }

    override fun getStudent(key: String): Student? { return getStudent(key, readableDatabase) }
    internal fun getStudent(key: String, db: SQLiteDatabase): Student? {
        val c = db.rawQuery("SELECT username, major, senioritylevel, emailaddress FROM ${QuizMaster.Constants.studentTable} WHERE username ='${encode(key)}'", null)
        c.moveToFirst()
        if (!c.isAfterLast) {
            return decode(Student(c.getString(0), c.getString(1), c.getString(2), c.getString(3)))
        }
        return null
    }

    override fun getQuiz(key: String): Quiz? { return getQuiz(key, readableDatabase) }
    internal fun getQuiz(key: String, db: SQLiteDatabase): Quiz? {

        val quizCursor = db!!.rawQuery("SELECT name, description, owner FROM ${QuizMaster.Constants.quizTable} WHERE name ='${encode(key)}'", null)
        if (quizCursor.moveToFirst()) {
            val name = quizCursor.getString(0)
            val description = quizCursor.getString(1)

            //other tables:
            val student = encode(getStudent(decode(quizCursor.getString(2)), db)!!)
            val words = getWords(key, db)!!.map { word -> encode(word) }
            val definitions = getDefinitions(key, db)!!.map { definition -> encode(definition) }

            return decode(Quiz(student, name, description, words, definitions))
        }

        return null
    }


    override fun getQuizNames(): List<Quiz> {
        val result = mutableListOf<Quiz>()
        val db = readableDatabase

        val quizCursor = db!!.rawQuery("SELECT name, description, owner FROM ${QuizMaster.Constants.quizTable}", null)
        if (quizCursor.moveToFirst()) {

            do {
                val name = quizCursor.getString(0)
                val description = quizCursor.getString(1)

                //other tables:
                val student = encode(getStudent(decode(quizCursor.getString(2)), db)!!)
                val words = getWords(decode(name), db)!!.map { word -> encode(word) }
                val definitions = getDefinitions(decode(name), db)!!.map { definition -> encode(definition) }

                result.add(decode(Quiz(student, name, description, words, definitions)))
            } while (quizCursor.moveToNext())
        }
        return result
    }

    fun getWords(key: String, db: SQLiteDatabase): List<Word>?{
        val words: MutableList<Word> = mutableListOf()
        val wordCursor = db!!.rawQuery("SELECT value, definition FROM ${QuizMaster.Constants.wordTable} WHERE quiz ='${encode(key)}'", null)
        if (wordCursor.moveToFirst()) {
            do {
                words.add(decode(Word(wordCursor.getString(0), wordCursor.getString(1))))
            } while (wordCursor.moveToNext())
            return words
        }
        return null
    }

    fun getDefinitions(key: String, db: SQLiteDatabase): List<String>? {
        val definitions: MutableList<String> = mutableListOf()
        val definitionCursor = db!!.rawQuery("SELECT value FROM ${QuizMaster.Constants.definitionTable} WHERE quiz ='${encode(key)}'", null)
        if (definitionCursor.moveToFirst()) {
            do {
                definitions.add(decode(definitionCursor.getString(0)))
            } while (definitionCursor.moveToNext())
            return definitions
        }
        return null
    }

    override fun getBestScoresByQuiz(quiz: String): List<Score>? {
        val db = readableDatabase

        val quizModel = getQuiz(quiz, db)!!
        val scores: MutableList<Score> = mutableListOf()
        val scoreCursor = db!!.rawQuery("SELECT percentage, ts, student FROM ${QuizMaster.Constants.scoreTable} WHERE quiz = '${encode(quiz)}' AND percentage = 1 ORDER BY student ASC LIMIT 3", null)
        if (scoreCursor.moveToFirst()) {
            do {
                val studentModel = getStudent(decode(scoreCursor.getString(2)), db)!!
                scores.add(decode(Score(encode(studentModel), encode(quizModel), scoreCursor.getDouble(0), scoreCursor.getLong(1))))
            } while (scoreCursor.moveToNext())
            return scores
        }
        return null
    }

    //todo this could be more efficient
    //todo does distinct preserve first-seen ordering?
    override fun getScoreOrderedQuizzes(student: String): List<Quiz> {
        val db = readableDatabase
        val quizzes: MutableList<Quiz> = mutableListOf<Quiz>()
        val quizCursor = db!!.rawQuery("""
            SELECT distinct owner, name, description FROM (
                SELECT 2 grouping, owner, name, description, ts FROM
                (
                    SELECT owner, name, description, MAX(s.ts) ts
                    FROM ${QuizMaster.Constants.quizTable} q INNER JOIN (SELECT * FROM ${QuizMaster.Constants.scoreTable} WHERE student =  '${encode(student)}') s ON q.name = s.quiz
                    GROUP BY owner, name, description
                    ORDER BY MAX(s.ts) DESC
                ) myQuizzes
                UNION
                SELECT 1 grouping, owner, name, description, ts FROM
                (
                    SELECT owner, name, description, MAX(s.ts) ts
                    FROM ${QuizMaster.Constants.quizTable} q LEFT OUTER JOIN ${QuizMaster.Constants.scoreTable} s ON q.name = s.quiz
                    WHERE s.student IS NULL or s.student != '${encode(student)}'
                    GROUP BY owner, name, description
                ) theirQuzzes
                ORDER BY grouping DESC, ts DESC
            ) allQuizzes

        """.trimIndent(), null)
        if (quizCursor.moveToFirst()) {
            do {
                quizzes.add(getQuiz(decode(quizCursor.getString(1)), db)!!)
            } while (quizCursor.moveToNext())
            return quizzes
        }
        return listOf<Quiz>()
    }

    override fun getScores(student: String, quiz: String): List<Score>? {
        val db = readableDatabase

        val quizModel = encode(getQuiz(quiz, db)!!)
        val studentModel = encode(getStudent(student, db)!!)

        val scores: MutableList<Score> = mutableListOf()
        val scoreCursor = db!!.rawQuery("SELECT percentage, ts FROM ${QuizMaster.Constants.scoreTable} WHERE student = '${encode(student)}' AND quiz ='${encode(quiz)}'", null)
        if (scoreCursor.moveToFirst()) {
            do {
                scores.add(decode(Score(studentModel, quizModel, scoreCursor.getDouble(0), scoreCursor.getLong(1))))
            } while (scoreCursor.moveToNext())
            return scores
        }
        return null
    }

    override fun putStudent(student: Student): Boolean {
        val safeStudent = encode(student)
        if(getStudent(safeStudent.username) != null) {
            return false
        }
        writableDatabase!!.execSQL("INSERT INTO ${QuizMaster.Constants.studentTable} VALUES ('${safeStudent.username}', '${safeStudent.major}', '${safeStudent.seniorityLevel}', '${safeStudent.emailAddress}')")
        return true
    }

    override fun putQuiz(quiz: Quiz): Boolean {
        val safeQuiz = encode(quiz)
        if(getQuiz(safeQuiz.name) != null) {
            return false
        }
        val db = writableDatabase

        begin(db!!)
        try{
            //create quiz:
            db!!.execSQL("INSERT INTO ${QuizMaster.Constants.quizTable} VALUES ('${safeQuiz.owner.username}', '${safeQuiz.name}', '${safeQuiz.description}')")

            //add the associated words & definitions:
            safeQuiz.words.forEach { word -> db!!.execSQL("INSERT INTO ${QuizMaster.Constants.wordTable} VALUES ('${word.value}', '${word.definition}', '${safeQuiz.name}')") }
            safeQuiz.definitions.forEach { definition -> db!!.execSQL("INSERT INTO ${QuizMaster.Constants.definitionTable} VALUES ('${definition}', '${safeQuiz.name}')") }

            commit(db!!)
        } catch(e: Exception) {
            rollback(db!!)
        } finally {
            db.close()
        }
        return true
    }


    override fun putScore(score: Score): Boolean {
        val safeScore = encode(score)
        writableDatabase!!.execSQL("INSERT INTO ${QuizMaster.Constants.scoreTable} VALUES ('${safeScore.student.username}', '${safeScore.quiz.name}', ${safeScore.percentage}, ${sqliteNow} )")
        return true
    }

    override fun dropQuiz(quiz: Quiz) {
        val safeQuiz = encode(quiz)
        val db = writableDatabase

        begin(db!!)
        try {
            db!!.execSQL("DELETE FROM ${QuizMaster.Constants.wordTable} WHERE quiz = '${safeQuiz.name}'")
            db!!.execSQL("DELETE FROM ${QuizMaster.Constants.definitionTable} WHERE quiz = '${safeQuiz.name}'")
            db!!.execSQL("DELETE FROM ${QuizMaster.Constants.quizTable} WHERE name = '${safeQuiz.name}'")
            commit(db!!)
        } catch(e: Exception) {
            rollback(db!!)
        } finally {
            db.close()
        }
    }

    /**
     *
     * Methods for base64 encode/decode strings inside DB
     * todo: maybe track whether it's been encoded in some way? this seems unsafe
     */

    //string encode/decode:
    private fun encode(s: String): String {
        return Base64.encodeToString(s.toByteArray(charset("UTF-8")), Base64.DEFAULT)
    }
    private fun decode(s: String): String {
        val data = Base64.decode(s, Base64.DEFAULT)
        return String(data, charset("UTF-8"))
    }

    //student encode/decode:
    private fun encode(student: Student): Student {
        return Student(encode(student.username), encode(student.major), encode(student.seniorityLevel), encode(student.emailAddress))
    }
    private fun decode(student: Student): Student {
        return Student(decode(student.username), decode(student.major), decode(student.seniorityLevel), decode(student.emailAddress))
    }

    //words encode/decode:
    private fun encode(word: Word): Word {
        return Word(encode(word.value), encode(word.definition))
    }
    private fun decode(word: Word): Word {
        return Word(decode(word.value), decode(word.definition))
    }

    //quiz encode/decode:
    private fun encode(quiz: Quiz): Quiz {
        return Quiz(encode(quiz.owner), encode(quiz.name), encode(quiz.description), quiz.words.map { word -> encode(word) }, quiz.definitions.map { definition -> encode(definition) })
    }
    private fun decode(quiz: Quiz): Quiz  {
        return Quiz(decode(quiz.owner), decode(quiz.name), decode(quiz.description), quiz.words.map { word -> decode(word) }, quiz.definitions.map { definition -> decode(definition) })
    }

    //score encode/decode:
    private fun encode(score: Score): Score {
        return Score(encode(score.student), encode(score.quiz), score.percentage, score.date)
    }
    private fun decode(score: Score): Score {
        return Score(decode(score.student), decode(score.quiz), score.percentage, score.date)
    }

    //transaction functions:
    private fun begin(db: SQLiteDatabase) {
        db.execSQL("BEGIN")
    }
    private fun rollback(db: SQLiteDatabase) {
        db.execSQL("ROLLBACK")
    }
    private fun commit(db: SQLiteDatabase) {
        db.execSQL("COMMIT")
    }
}