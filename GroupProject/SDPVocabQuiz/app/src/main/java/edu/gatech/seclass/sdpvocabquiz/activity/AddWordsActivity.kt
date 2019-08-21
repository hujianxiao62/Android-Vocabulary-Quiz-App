package edu.gatech.seclass.sdpvocabquiz.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import edu.gatech.seclass.sdpvocabquiz.QuizMaster
import edu.gatech.seclass.sdpvocabquiz.R
import edu.gatech.seclass.sdpvocabquiz.adapter.AddListAdapter
import edu.gatech.seclass.sdpvocabquiz.adt.Quiz
import edu.gatech.seclass.sdpvocabquiz.adt.Word
import edu.gatech.seclass.sdpvocabquiz.database.DatabaseManager
import edu.gatech.seclass.sdpvocabquiz.database.SqliteDatabaseManager
import kotlinx.android.synthetic.main.activity_add_quiz_title.*
import kotlinx.android.synthetic.main.activity_add_words.*

class AddWordsActivity : AppCompatActivity() {

    private var currentWordList: MutableList<Word> = arrayListOf()
    private lateinit var quizTitle: String
    private lateinit var quizDescription: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_words)

        quizTitle = QuizMaster.quizTitle!!
        quizDescription = QuizMaster.quizDescription!!

        btn_AddWordsAdd.setOnClickListener {
            val selectedWord = Word(txt_addWordsWord.text.toString(), txt_addWordsDef.text.toString())
            if(!selectedWord.value.isEmpty() && !selectedWord.definition.isEmpty()) {
                if(!currentWordList.map { word -> word.value }.contains(selectedWord.value)) {
                    if(!currentWordList.map { word -> word.definition }.contains(selectedWord.definition)) {
                        if(currentWordList.size < 10) {
                            currentWordList.add(selectedWord)
                            txt_addWordsWord.text.clear()
                            txt_addWordsDef.text.clear()
                            refreshData()
                        } else {
                            QuizMaster.toastPrint(this, getString(R.string.toast_You_already_have_10_words))
                        }
                    } else {
                        QuizMaster.toastPrint(this, getString(R.string.toast_That_definition_has_already_been_entered))
                    }
                } else {
                    QuizMaster.toastPrint(this, getString(R.string.toast_That_word_has_already_been_entered))
                }
            } else {
                QuizMaster.toastPrint(this, getString(R.string.toast_Please_enter_word_value_and_definition))
            }
        }

        btn_AddWordsDelete.setOnClickListener {
            val selectedWord = Word(txt_addWordsWord.text.toString(),txt_addWordsDef.text.toString())
            currentWordList.remove(selectedWord)
            txt_addWordsWord.text.clear()
            txt_addWordsDef.text.clear()
            refreshData()

        }

        btn_AddWordsDone.setOnClickListener {
            if(currentWordList.size > 0 && currentWordList.size <= 10) {
                QuizMaster.toastPrint(this, "Please input ${currentWordList.size*3} incorrect definitions")
                QuizMaster.showAddDefintions(this, quizTitle, quizDescription, currentWordList)
            }
            else{
                QuizMaster.toastPrint(this, getString(R.string.toast_Please_input_1_10_words_and_definitions))
            }

        }
    }

    private fun refreshData(){
        val adapter = AddListAdapter(this@AddWordsActivity,currentWordList,txt_addWordsWord,txt_addWordsDef)
        list_addWord.adapter = adapter
    }
}
