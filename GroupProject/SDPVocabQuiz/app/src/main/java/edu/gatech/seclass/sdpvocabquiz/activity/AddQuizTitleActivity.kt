package edu.gatech.seclass.sdpvocabquiz.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import edu.gatech.seclass.sdpvocabquiz.QuizMaster
import edu.gatech.seclass.sdpvocabquiz.R
import edu.gatech.seclass.sdpvocabquiz.adt.Quiz
import edu.gatech.seclass.sdpvocabquiz.database.DatabaseManager
import edu.gatech.seclass.sdpvocabquiz.database.SqliteDatabaseManager
import kotlinx.android.synthetic.main.activity_add_quiz_title.*

class AddQuizTitleActivity : AppCompatActivity() {

    private lateinit var quizTitle: String
    private lateinit var quizDescription: String
    private lateinit var db: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quiz_title)
        db = SqliteDatabaseManager(this)

        btn_addQuizNext.setOnClickListener {
            quizTitle = txt_addQuizTitle.text.toString()
            quizDescription = txt_addQuizDescription.text.toString()

            if(quizTitle != null && !quizTitle.isEmpty() && quizDescription != null && !quizDescription.isEmpty()){
                if(db.getQuiz(quizTitle) != null) {
                    QuizMaster.toastPrint(this, getString(R.string.toast_A_quiz_with_that_name_already_exists))
                } else {
                    QuizMaster.toastPrint(this, getString(R.string.toast_Please_input_1_10_words_and_definitions))
                    QuizMaster.showAddWords(this, quizTitle, quizDescription)
                }
            } else {
                QuizMaster.toastPrint(this, getString(R.string.toast_Please_input_Quiz_title_and_description))
            }
        }
    }
}
