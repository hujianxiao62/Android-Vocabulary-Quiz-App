package edu.gatech.seclass.sdpvocabquiz

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Button
import android.widget.Toast
import edu.gatech.seclass.sdpvocabquiz.activity.*
import edu.gatech.seclass.sdpvocabquiz.adt.Quiz
import edu.gatech.seclass.sdpvocabquiz.adt.Student
import edu.gatech.seclass.sdpvocabquiz.adt.Word
import java.text.SimpleDateFormat

object QuizMaster {

    object Constants {
        //database constants:
        val dbName = "sdpteamtwelve"
        val dbVersion = 7

        val quizTable = "quiz"
        val scoreTable = "score"
        val studentTable = "student"
        val wordTable = "word"
        val definitionTable = "definition"
        val tables = arrayOf(quizTable, scoreTable, studentTable, wordTable, definitionTable)

        //[[SimpleDateFormat]] for score timestamps
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        //constants for controlling list view:
        val DELETE_MODE = "delete"
        val PRACTICE_MODE = "practice"
        val SCORE_MODE = "score"
    }

    /**
     *
     * Variables to maintain state:
     *
     */
    //tracks logged in student:
    var loggedInStudent: Student? = null

    //tracks registration username:
    var username: String? = null

    //track state for quiz practice:
    var currentQuiz: Quiz? = null
    var originalQuiz: Quiz? = null
    var score: Int = 0

    //track state for quiz creation:
    var quizTitle: String? = null
    var quizDescription: String? = null
    var wordList: List<Word> = listOf()

    //tracks quiz for score viewing:
    var scoreQuiz: Quiz? = null

    //tracks mode used for listing:
    var viewMode: String? = null

    /**
     *
     * Functions to change the current activity:
     *
     */
    fun showMenu(context: Context) {
        val intent = Intent(context, MenuActivity::class.java)
        context.startActivity(intent)
    }

    fun showLogin(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }

    fun showRegister(context: Context, username: String) {
        val intent = Intent(context, RegisterActivity::class.java)
        this.username = username
        context.startActivity(intent)
    }

    fun showAddQuiz(context: Context) {
        val intent = Intent(context, AddQuizTitleActivity::class.java)
        context.startActivity(intent)
    }

    fun showPracticeList(context: Context) {
        val intent = Intent(context, ListActivity::class.java)
        this.viewMode = Constants.PRACTICE_MODE
        context.startActivity(intent)
    }

    fun showDeleteList(context: Context) {
        val intent = Intent(context, ListActivity::class.java)
        this.viewMode = Constants.DELETE_MODE
        context.startActivity(intent)
    }

    fun showScoreList(context: Context) {
        val intent = Intent(context, ListActivity::class.java)
        this.viewMode = Constants.SCORE_MODE
        context.startActivity(intent)
    }

    fun showAddDefintions(context: Context, quizTitle: String, quizDescription: String, currentWordList: List<Word>) {
        val intent = Intent(context, AddWrongActivity::class.java)
        this.quizTitle = quizTitle
        this.quizDescription = quizDescription
        this.wordList = currentWordList.toList()
        context.startActivity(intent)
    }

    fun showAddWords(context: Context, quizTitle: String, quizDescription: String) {
        val intent = Intent(context, AddWordsActivity::class.java)
        this.quizTitle = quizTitle
        this.quizDescription = quizDescription
        context.startActivity(intent)
    }

    fun showPracticeQuiz(context: Context, nextQuiz: Quiz, correct: Boolean) {
        val intent = Intent(context, PracticeActvity::class.java)
        currentQuiz = nextQuiz
        if(correct) {
            score += 1
            toastPrint(context, "Correct!")
        } else {
            toastPrint(context, "Incorrect :(")
        }
        context.startActivity(intent)
    }

    fun showPracticeQuiz(context: Context, quiz: Quiz) {
        val intent = Intent(context, PracticeActvity::class.java)
        this.currentQuiz = quiz
        this.originalQuiz = quiz
        this.score = 0
        context.startActivity(intent)
    }

    fun showScore(context: Context, quiz: Quiz) {
        val intent = Intent(context, ViewScoreActivity::class.java)
        this.scoreQuiz = quiz
        context.startActivity(intent)
    }

    fun toastPrint(context: Context, value: String) {
        Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
    }

}