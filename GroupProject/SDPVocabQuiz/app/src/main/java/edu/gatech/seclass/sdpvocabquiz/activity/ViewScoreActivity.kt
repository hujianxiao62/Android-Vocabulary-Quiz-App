package edu.gatech.seclass.sdpvocabquiz.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import edu.gatech.seclass.sdpvocabquiz.QuizMaster
import edu.gatech.seclass.sdpvocabquiz.R
import edu.gatech.seclass.sdpvocabquiz.adt.Quiz
import edu.gatech.seclass.sdpvocabquiz.adt.Score
import edu.gatech.seclass.sdpvocabquiz.database.DatabaseManager
import edu.gatech.seclass.sdpvocabquiz.database.SqliteDatabaseManager

//can only be invoked if extra("quiz") and [[QuizMaster.loggedInStudent]] are set
class ViewScoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_score)

        val db: DatabaseManager = SqliteDatabaseManager(this)

        //get quiz:
        val quiz: Quiz = db.getQuiz(QuizMaster.scoreQuiz!!.name)!!
        findViewById<TextView>(R.id.view_score_quiz).text = quiz.name

        //get my scores:
        val myScores: List<Score>? = db.getScores(QuizMaster.loggedInStudent!!.username, quiz.name)
        if(myScores != null) {
            val bestScore = myScores!!.maxBy { score -> score.percentage }!!
            val firstScore = myScores!!.minBy { score -> score.date }!!
            findViewById<TextView>(R.id.view_score_best_time).text = QuizMaster.Constants.format.format(bestScore.date)
            findViewById<TextView>(R.id.view_score_best_score).text = Math.round(bestScore.percentage * 100).toString()
            findViewById<TextView>(R.id.view_score_first_time).text = QuizMaster.Constants.format.format(firstScore.date)
            findViewById<TextView>(R.id.view_score_first_score).text = Math.round(firstScore.percentage * 100).toString()
//            findViewById<ProgressBar>(R.id.view_score_visual).progress = Math.round(bestScore.percentage * 100).toInt()
        }

        val topScores = db.getBestScoresByQuiz(quiz.name)
        if (topScores != null && !topScores.isEmpty()) {
            findViewById<TextView>(R.id.view_score_one_time).text = QuizMaster.Constants.format.format(topScores!![0].date)
            findViewById<TextView>(R.id.view_score_one_student).text = topScores!![0].student.username
            findViewById<TextView>(R.id.view_score_one_score).text = Math.round(topScores!![0].percentage * 100).toString()
            if(topScores.size >= 2) {
                findViewById<TextView>(R.id.view_score_two_time).text = QuizMaster.Constants.format.format(topScores!![1].date)
                findViewById<TextView>(R.id.view_score_two_student).text = topScores!![1].student.username
                findViewById<TextView>(R.id.view_score_two_score).text = Math.round(topScores!![1].percentage * 100).toString()
            }
            if(topScores.size >= 3) {
                findViewById<TextView>(R.id.view_score_three_time).text = QuizMaster.Constants.format.format(topScores!![2].date)
                findViewById<TextView>(R.id.view_score_three_student).text = topScores!![2].student.username
                findViewById<TextView>(R.id.view_score_three_score).text = Math.round(topScores!![2].percentage * 100).toString()
            }
        }






    }

}
