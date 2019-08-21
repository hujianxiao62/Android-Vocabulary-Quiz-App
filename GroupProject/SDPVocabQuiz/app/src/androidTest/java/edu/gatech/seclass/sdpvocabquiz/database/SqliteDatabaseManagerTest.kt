package edu.gatech.seclass.sdpvocabquiz.database

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import edu.gatech.seclass.sdpvocabquiz.adt.Quiz
import edu.gatech.seclass.sdpvocabquiz.adt.Score
import edu.gatech.seclass.sdpvocabquiz.adt.Student
import edu.gatech.seclass.sdpvocabquiz.adt.Word

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.logging.Logger
import org.junit.After



/**
 * Used to test database functionality in [[SqliteDatabaseManager]]
 */
@RunWith(AndroidJUnit4::class)
class SqliteDatabaseManagerTest {

    private lateinit var db: SqliteDatabaseManager
    val log = Logger.getLogger(SqliteDatabaseManagerTest::class.java.name)

    @Before
    fun setUp() {
        db = SqliteDatabaseManager(InstrumentationRegistry.getTargetContext())
        resetDatabase()
    }

    @After
    fun finish() {
        resetDatabase()
    }

    fun resetDatabase() {
        db.dropTables()
        db.createTables()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("edu.gatech.seclass.sdpvocabquiz", appContext.packageName)
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                   I/O Tests                                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //make sure students can be stored and retrieved:
    @Test
    fun addStudent() {
        resetDatabase()
        val student = Student("u", "m", "s", "e")
        db.putStudent(student)
        val result = db.getStudent(student.username)
        assertEquals(result, student)
    }

    //make sure multiple students can be stored and retrieved:
    @Test
    fun addStudents() {
        resetDatabase()
        val student1 = Student("1", "2", "3", "4")
        val student2 = Student("a", "b", "c", "d")
        db.putStudent(student1)
        db.putStudent(student2)
        assertEquals(student1, db.getStudent(student1.username))
        assertEquals(student2, db.getStudent(student2.username))
    }

    //make sure quizzes can be stored and retrieved:
    @Test
    fun addQuiz() {
        resetDatabase()
        val student = Student("u", "m", "s", "e")
        val word = Word("wing", "ding")
        val quiz = Quiz(student, "foo", "bar", listOf<Word>(word), listOf<String>("a", "b", "c"))
        db.putStudent(student)
        db.putQuiz(quiz)
        val quizzes = db.getQuizNames()
        assertEquals(1, quizzes.size)
        assertEquals(quiz, quizzes[0])
    }

    //make sure multiple quizzes can be stored and retrieved:
    @Test
    fun addQuizzes() {
        resetDatabase()
        val student = Student("u", "m", "s", "e")
        val quiz1 = Quiz(student, "foo", "bar", listOf<Word>(Word("wing", "ding")), listOf<String>("a", "b", "c"))
        val quiz2 = Quiz(student, "baz", "bar", listOf<Word>(Word("ping", "pong")), listOf<String>("1", "2", "3"))
        db.putStudent(student)
        db.putQuiz(quiz1)
        db.putQuiz(quiz2)
        val quizzes = db.getQuizNames()
        assertEquals(2, quizzes.size)
        assertEquals(quizzes[0], quiz1)
        assertEquals(quizzes[1], quiz2)
    }

    //make sure definitions with the same value are preserved so long as the quizzes are different:
    @Test
    fun addDefinitionsSameValue() {
        resetDatabase()
        val student = Student("u", "m", "s", "e")
        val quiz1 = Quiz(student, "foo", "bar", listOf<Word>(Word("wing", "ding")), listOf<String>("a", "b", "c"))
        val quiz2 = Quiz(student, "baz", "bar", listOf<Word>(Word("ping", "pong")), listOf<String>("a", "b", "c"))
        db.putStudent(student)
        db.putQuiz(quiz1)
        db.putQuiz(quiz2)
        val quizzes = db.getQuizNames()
        assertEquals(2, quizzes.size)
        assertEquals(quizzes[0], quiz1)
        assertEquals(quizzes[1], quiz2)
    }

    //make sure words with the same value are preserved so long as the quizzes are different:
    @Test
    fun addWordsSameValue() {
        resetDatabase()
        val student = Student("u", "m", "s", "e")
        val quiz1 = Quiz(student, "foo", "bar", listOf<Word>(Word("wing", "ding")), listOf<String>("a", "b", "c"))
        val quiz2 = Quiz(student, "baz", "bar", listOf<Word>(Word("wing", "ding")), listOf<String>("1", "2", "3"))
        db.putStudent(student)
        db.putQuiz(quiz1)
        db.putQuiz(quiz2)
        val quizzes = db.getQuizNames()
        assertEquals(2, quizzes.size)
        assertEquals(quizzes[0], quiz1)
        assertEquals(quizzes[1], quiz2)
    }

    //make sure scores can be stored and retrieved:
    @Test
    fun addScore() {
        resetDatabase()

        val student = Student("u", "m", "s", "e")
        val quiz = Quiz(student, "foo", "bar", listOf<Word>(Word("wing", "ding")), listOf<String>("a", "b", "c"))
        val score = Score(student, quiz, 0.0, 0L)

        assert(db.putStudent(student))
        assert(db.putQuiz(quiz))
        assert(db.putScore(score))

        val result = db.getScores(student.username, quiz.name)!!
        log.info(result.toString())

        assertEquals(1, result.size)
        assertEquals((score.percentage * 100).toInt(), (result[0].percentage * 100).toInt())
        assertEquals(score.student, result[0].student)
        assertEquals(score.quiz, result[0].quiz)
    }

    //make sure multiple scores can be stored and retrieved with the same student and quiz:
    @Test
    fun addScores() {
        resetDatabase()

        val student = Student("u", "m", "s", "e")
        val quiz = Quiz(student, "foo", "bar", listOf<Word>(Word("wing", "ding")), listOf<String>("a", "b", "c"))
        val score1 = Score(student, quiz, 0.1, 0L)
        val score2 = Score(student, quiz, 0.2, 0L)

        db.putStudent(student)
        db.putQuiz(quiz)
        db.putScore(score1)
        Thread.sleep(100)
        db.putScore(score2)

        val result = db.getScores(student.username, quiz.name)!!
        log.info(result.toString())

        assertEquals(2, result.size)
        assert(result[0].percentage == .1 || result[1].percentage == .1)
        assert(result[0].percentage == .2 || result[1].percentage == .2)
        assertEquals(student, result[0].student)
        assertEquals(student, result[1].student)
        assertEquals(quiz, result[0].quiz)
        assertEquals(quiz, result[1].quiz)
    }

    //make sure quizzes are retrieved in the right order:
    @Test
    fun retieveQuizzesByScore() {
        resetDatabase()

        val student = Student("u", "m", "s", "e")

        val myQuiz1 = Quiz(student, "my1", "bar", listOf<Word>(Word("wing", "ding")), listOf<String>("a", "b", "c"))
        val myQuiz2 = Quiz(student, "my2", "bar", listOf<Word>(Word("ping", "pong")), listOf<String>("a", "b", "c"))
        val myQuiz3 = Quiz(student, "my3", "bar", listOf<Word>(Word("king", "kong")), listOf<String>("a", "b", "c"))
        val myScore1 = Score(student, myQuiz1, 0.0, 0L)
        val myScore2 = Score(student, myQuiz2, 0.0, 0L)
        val myScore3 = Score(student, myQuiz3, 0.0, 0L)
        val myScore4 = Score(student, myQuiz2, 0.0, 0L)

        val otherQuiz1 = Quiz(student, "other1", "bar", listOf<Word>(Word("sing", "song")), listOf<String>("a", "b", "c"))
        val otherQuiz2 = Quiz(student, "other2", "bar", listOf<Word>(Word("sing", "song")), listOf<String>("a", "b", "c"))

        db.putStudent(student)
        db.putQuiz(otherQuiz1)
        db.putQuiz(myQuiz1)
        db.putQuiz(myQuiz3)
        db.putQuiz(otherQuiz2)
        db.putQuiz(myQuiz2)
        db.putScore(myScore1)
        db.putScore(myScore2)
        db.putScore(myScore3)
        db.putScore(myScore4)

        val myQuizSlice = arrayOf(myQuiz2, myQuiz3, myQuiz1)
        val quizzes = db.getScoreOrderedQuizzes(student.username)

        log.info("Retrieved quizzes:")
        quizzes.forEach { quiz -> log.info(quiz.toString()) }
        assertEquals(5, quizzes.size)
        assertEquals(true, myQuizSlice.contentDeepEquals(quizzes.subList(0, 3).toTypedArray()))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                   Constraint Tests                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //todo test db constraints
}
