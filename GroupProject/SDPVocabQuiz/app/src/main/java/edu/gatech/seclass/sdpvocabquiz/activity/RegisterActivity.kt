package edu.gatech.seclass.sdpvocabquiz.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import edu.gatech.seclass.sdpvocabquiz.QuizMaster
import edu.gatech.seclass.sdpvocabquiz.R
import edu.gatech.seclass.sdpvocabquiz.R.id.*
import edu.gatech.seclass.sdpvocabquiz.adt.Quiz
import edu.gatech.seclass.sdpvocabquiz.adt.Student
import edu.gatech.seclass.sdpvocabquiz.database.DatabaseManager
import edu.gatech.seclass.sdpvocabquiz.database.SqliteDatabaseManager

class RegisterActivity : AppCompatActivity() {

    private lateinit var db: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        db = SqliteDatabaseManager(this)
        findViewById<TextView>(txt_registerUserName).text = QuizMaster.username

        findViewById<Button>(btn_registerDone).setOnClickListener {
            val username = findViewById<TextView>(txt_registerUserName).text.toString()
            val major = findViewById<TextView>(txt_registerMajor).text.toString()
            val seniorityLevel = findViewById<Spinner>(spinner_registerSeniority).selectedItem.toString()
            val email = findViewById<TextView>(txt_reigsterEmail).text.toString()
            if(!username.isEmpty() && !major.isEmpty() && !seniorityLevel.isEmpty() && !email.isEmpty()) {
                val student = Student(username, major, seniorityLevel, email)
                QuizMaster.loggedInStudent = student
                if(db.putStudent(student)) {
                    QuizMaster.toastPrint(this, getString(R.string.toast_Register_Success))
                    QuizMaster.showMenu(this)
                } else {
                    QuizMaster.toastPrint(this, getString(R.string.toast_A_student_with_that_name_already_exists))

                }
            } else {
                QuizMaster.toastPrint(this, getString(R.string.toast_Please_populate_all_fields))
            }
        }
    }
}
