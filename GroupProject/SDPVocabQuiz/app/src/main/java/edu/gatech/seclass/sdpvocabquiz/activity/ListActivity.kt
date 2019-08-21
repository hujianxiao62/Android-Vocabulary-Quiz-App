package edu.gatech.seclass.sdpvocabquiz.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import edu.gatech.seclass.sdpvocabquiz.QuizMaster
import edu.gatech.seclass.sdpvocabquiz.R
import edu.gatech.seclass.sdpvocabquiz.adapter.DeleteListAdapter
import edu.gatech.seclass.sdpvocabquiz.adapter.PracticeListAdapter
import edu.gatech.seclass.sdpvocabquiz.adapter.ScoreListAdapter
import edu.gatech.seclass.sdpvocabquiz.database.DatabaseManager
import edu.gatech.seclass.sdpvocabquiz.database.SqliteDatabaseManager
import kotlinx.android.synthetic.main.activity_quiz_list.*

class ListActivity : AppCompatActivity() {

    private lateinit var db: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_list)

        //get db instance:
        db = SqliteDatabaseManager(this)

        //add adapter:
        refreshData()
    }

    private fun refreshData() {
        when (QuizMaster.viewMode!!) {
            QuizMaster.Constants.PRACTICE_MODE -> {
                val quizzes = db.getQuizNames().filter { quiz -> quiz.owner != QuizMaster.loggedInStudent }
                if (quizzes.isEmpty()) {
                    QuizMaster.toastPrint(this, getString(R.string.toast_Sorry_nobody_else_has_created_any_quizzes_yet))
                }
                txt_list_element.adapter = PracticeListAdapter(this@ListActivity, quizzes, db)
            }
            QuizMaster.Constants.DELETE_MODE -> {
                val quizzes = db.getQuizNames().filter { quiz -> quiz.owner == QuizMaster.loggedInStudent }
                txt_list_element.adapter = DeleteListAdapter(this@ListActivity, quizzes, db)
            }
            QuizMaster.Constants.SCORE_MODE -> {
                txt_list_element.adapter = ScoreListAdapter(this@ListActivity, db.getScoreOrderedQuizzes(QuizMaster.loggedInStudent!!.username), db)
            }
        }
    }
}
