package edu.gatech.seclass.sdpvocabquiz.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import edu.gatech.seclass.sdpvocabquiz.QuizMaster
import edu.gatech.seclass.sdpvocabquiz.R
import edu.gatech.seclass.sdpvocabquiz.activity.ViewScoreActivity
import edu.gatech.seclass.sdpvocabquiz.adt.Score
import edu.gatech.seclass.sdpvocabquiz.adt.Quiz
import edu.gatech.seclass.sdpvocabquiz.database.DatabaseManager
import kotlinx.android.synthetic.main.listview_quiz.view.*

class ScoreListAdapter(private val activity: Activity,
                       private val quizzes: List<Quiz>,
                       private val db: DatabaseManager
                        ):BaseAdapter(){

    private val inflater: LayoutInflater

    init{
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.listview_quiz, null)
        rowView.txt_list_sub_element.text = quizzes[position].name
        rowView.setOnClickListener { QuizMaster.showScore(activity, quizzes[position]) }
        return rowView
    }

    override fun getItem(position: Int): Quiz {
        return quizzes[position]
    }

    override fun getItemId(position: Int): Long {
        return quizzes[position].hashCode().toLong()
    }

    override fun getCount(): Int {
        return quizzes.size
    }


}