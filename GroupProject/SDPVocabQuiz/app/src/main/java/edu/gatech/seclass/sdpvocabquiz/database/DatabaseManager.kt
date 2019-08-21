package edu.gatech.seclass.sdpvocabquiz.database

import edu.gatech.seclass.sdpvocabquiz.adt.Quiz
import edu.gatech.seclass.sdpvocabquiz.adt.Score
import edu.gatech.seclass.sdpvocabquiz.adt.Student

interface DatabaseManager {

    val createTableStatements: Map<String, String>

    fun getStudent(key: String): Student?
    fun getQuiz(key: String): Quiz?
    fun getQuizNames(): List<Quiz>

    fun getBestScoresByQuiz(quiz: String): List<Score>?
    fun getScores(student: String, quiz: String): List<Score>?
    fun getScoreOrderedQuizzes(student: String): List<Quiz>

    fun putStudent(student: Student): Boolean
    fun putQuiz(quiz: Quiz): Boolean
    fun putScore(score: Score): Boolean

    fun dropQuiz(quiz: Quiz)

    fun dropTables()
    fun createTables()

}