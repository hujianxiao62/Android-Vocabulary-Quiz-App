package edu.gatech.seclass.sdpvocabquiz.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import edu.gatech.seclass.sdpvocabquiz.QuizMaster
import edu.gatech.seclass.sdpvocabquiz.R

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        findViewById<TextView>(R.id.textMenuUsername).text = "Hello " + QuizMaster.loggedInStudent!!.username

        //add quiz:
        findViewById<Button>(R.id.btn_MenuAddQuiz).setOnClickListener {
            QuizMaster.showAddQuiz(this)
        }

        //practice quiz:
        findViewById<Button>(R.id.btn_MenuPracticeQuiz).setOnClickListener {
            QuizMaster.showPracticeList(this)
        }

        //delete quiz:
        findViewById<Button>(R.id.btn_menuRemoveQuiz).setOnClickListener {
            QuizMaster.showDeleteList(this)
        }

        //view quiz statistics:
        findViewById<Button>(R.id.btn_MenuViewScore).setOnClickListener {
            QuizMaster.showScoreList(this)
        }

        //log out:
        findViewById<Button>(R.id.btn_MenuLogout).setOnClickListener {
            QuizMaster.loggedInStudent = null
            QuizMaster.showLogin(this)
        }

    }
}
