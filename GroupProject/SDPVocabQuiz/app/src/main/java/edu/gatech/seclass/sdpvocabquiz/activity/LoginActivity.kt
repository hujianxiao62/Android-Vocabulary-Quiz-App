package edu.gatech.seclass.sdpvocabquiz.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import edu.gatech.seclass.sdpvocabquiz.QuizMaster
import edu.gatech.seclass.sdpvocabquiz.R
import edu.gatech.seclass.sdpvocabquiz.R.id.btn_loginGo
import edu.gatech.seclass.sdpvocabquiz.database.DatabaseManager
import edu.gatech.seclass.sdpvocabquiz.database.SqliteDatabaseManager

class LoginActivity : AppCompatActivity() {

    private lateinit var db: DatabaseManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        db = SqliteDatabaseManager(this)

        findViewById<Button>(btn_loginGo).setOnClickListener {
            val username = findViewById<TextView>(R.id.txt_loginUsername).text.toString()
            if(!username.isEmpty()) {
                val student = db.getStudent(username)
                if (student != null) {
                    QuizMaster.toastPrint(this, getString(R.string.toast_Login_Success))
                    QuizMaster.loggedInStudent = student
                    QuizMaster.showMenu(this)
                } else {
                    QuizMaster.toastPrint(this, getString(R.string.toast_Please_Register))
                    QuizMaster.showRegister(this, username)
                }
            } else {
                QuizMaster.toastPrint(this, getString(R.string.toast_Please_enter_a_username))
            }

        }


    }
}
