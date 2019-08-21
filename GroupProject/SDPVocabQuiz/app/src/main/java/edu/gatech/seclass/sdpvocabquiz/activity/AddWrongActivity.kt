package edu.gatech.seclass.sdpvocabquiz.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import edu.gatech.seclass.sdpvocabquiz.QuizMaster
import edu.gatech.seclass.sdpvocabquiz.R
import edu.gatech.seclass.sdpvocabquiz.adapter.AddListAdapter
import edu.gatech.seclass.sdpvocabquiz.adapter.AddWrongListAdapter
import edu.gatech.seclass.sdpvocabquiz.adt.Quiz
import edu.gatech.seclass.sdpvocabquiz.adt.Word
import edu.gatech.seclass.sdpvocabquiz.database.SqliteDatabaseManager
import kotlinx.android.synthetic.main.activity_add_quiz_title.*
import kotlinx.android.synthetic.main.activity_add_words.*
import kotlinx.android.synthetic.main.activity_add_wrong.*

class AddWrongActivity : AppCompatActivity() {

    private  var  currentWrongList:MutableList<String> = arrayListOf()
    private lateinit var db: SqliteDatabaseManager


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wrong)

        db = SqliteDatabaseManager(this)

        val quizTitle = QuizMaster.quizTitle!!
        val quizDescription = QuizMaster.quizDescription!!
        val words = QuizMaster.wordList
        val quizLength = words.size

        btn_AddWrongAdd.setOnClickListener {
            if(currentWrongList.size < quizLength * 3) {
                val definition: String = txt_addWrongDef.text.toString()
                if(!definition.isEmpty()) {
                    if(!words.map { word -> word.definition }.contains(definition)) {
                        if(!currentWrongList.contains(definition)) {
                            currentWrongList.add(definition)
                            txt_addWrongDef.text.clear()
                            refreshData()
                        } else {
                            QuizMaster.toastPrint(this, getString(R.string.toast_You_ve_already_used_that_definition))
                        }
                    } else {
                        val word = words.filter { word -> word.definition == definition }[0]
                        QuizMaster.toastPrint(this,getString(R.string.toast_That_is_the_definition_of_quiz_word))
                    }
                } else {
                    QuizMaster.toastPrint(this, getString(R.string.toast_Please_enter_a_value))
                }
            } else {
                QuizMaster.toastPrint(this,getString(R.string.toast_You_already_have_enough_definitions))
            }
        }

        btn_AddWrongDelete.setOnClickListener {
            val definition = txt_addWrongDef.text.toString()
            if(currentWrongList.contains(definition)) {
                currentWrongList.remove(definition)
                refreshData()
            } else {
                QuizMaster.toastPrint(this, getString(R.string.toast_Invalid_selection_to_delete))
            }
        }

        btn_AddWrongDone.setOnClickListener {
            if(currentWrongList.size == quizLength * 3){
                if(db.putQuiz(Quiz(QuizMaster.loggedInStudent!!, quizTitle, quizDescription, words, currentWrongList))) {
                    QuizMaster.toastPrint(this, getString(R.string.toast_Success))
                    QuizMaster.showMenu(this)
                } else {
                    QuizMaster.toastPrint(this, getString(R.string.toast_A_quiz_with_that_name_already_exists))
                    QuizMaster.showMenu(this)
                }

            }
            else{
                QuizMaster.toastPrint(this, "Please input ${(quizLength * 3) - currentWrongList.size} more incorrect definitions")
            }
        }
    }

    private fun refreshData() {
        val adapter = AddWrongListAdapter(this@AddWrongActivity, currentWrongList, txt_addWrongDef)
        list_addWrong.adapter = adapter
    }
}
