package edu.gatech.seclass.sdpvocabquiz.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import edu.gatech.seclass.sdpvocabquiz.QuizMaster
import edu.gatech.seclass.sdpvocabquiz.R
import edu.gatech.seclass.sdpvocabquiz.adt.Quiz
import edu.gatech.seclass.sdpvocabquiz.adt.Score
import edu.gatech.seclass.sdpvocabquiz.adt.Word
import edu.gatech.seclass.sdpvocabquiz.database.DatabaseManager
import edu.gatech.seclass.sdpvocabquiz.database.SqliteDatabaseManager
import java.io.Serializable
import kotlinx.android.synthetic.main.activity_practice_actvity.*
import java.util.*

class PracticeActvity : AppCompatActivity() {

    val random = Random()
    private lateinit var db: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_actvity)

        val originalQuiz = QuizMaster.originalQuiz!!
        val currentQuiz = QuizMaster.currentQuiz!!
        val score = QuizMaster.score

        //check if quiz is over
        if(currentQuiz.words.isEmpty()) {
            val percentage = score.toDouble() / originalQuiz.words.size
            val score = Score(QuizMaster.loggedInStudent!!, originalQuiz, percentage, 0L)
            db = SqliteDatabaseManager(this)
            if(!db.putScore(score)) {
                QuizMaster.toastPrint(this, getString(R.string.toast_An_error_occured_while_saving_your_score))
            }


            //go to menu:
            //todo have a landing page
            QuizMaster.toastPrint(this, "Quiz complete! Final score: " + (percentage * 100).toInt() + "%")
            QuizMaster.showMenu(this)
        } else {
            val wordIndex: Int = random.nextInt(currentQuiz.words.size)
            val badDefinitions = originalQuiz.definitions.plus(originalQuiz.words.minus(currentQuiz.words[wordIndex]).map { word -> word.definition })
            val badIndex: MutableList<Int> = mutableListOf<Int>()
            while(badIndex.size < 3) {
                val temp = random.nextInt(badDefinitions.size)
                if (!badIndex.contains(temp)) {
                    badIndex.add(temp)
                }
            }

            val nextWords = currentQuiz.words.minusElement(currentQuiz.words[wordIndex])
            val nextQuiz = Quiz(currentQuiz.owner, currentQuiz.name, currentQuiz.description, nextWords, currentQuiz.definitions)

            practice_question.text = currentQuiz.words[wordIndex].value
            practice_quizName.text = currentQuiz.name

            val progress =  ((originalQuiz.words.size - currentQuiz.words.size) + 1)
            practice_number.text = progress.toString() + " of " + originalQuiz.words.size
            practice_progress.progress = progress

            val answer = currentQuiz.words[wordIndex].definition
            val randomBag = mutableListOf<String>(
                    answer,
                    badDefinitions[badIndex[0]],
                    badDefinitions[badIndex[1]],
                    badDefinitions[badIndex[2]])
            randomBag.shuffle()

            //add listeners to each button:
            listOf(practice_one, practice_two, practice_three, practice_four)
                    .zip(randomBag)
                    .forEach { pair -> buildAnswerListener(pair.first, nextQuiz, answer, pair.second)}

        }
    }

    private fun buildAnswerListener(button: Button, nextQuiz: Quiz, answer: String, value: String) {
        button.text = value
        button.setOnClickListener {
            QuizMaster.showPracticeQuiz(this, nextQuiz, button.text == answer)
        }
    }
}
