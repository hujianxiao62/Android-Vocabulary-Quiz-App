package edu.gatech.seclass.sdpvocabquiz.adapter

import android.app.Activity
import android.content.Context
import android.provider.UserDictionary
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import edu.gatech.seclass.sdpvocabquiz.R
import edu.gatech.seclass.sdpvocabquiz.adt.Quiz
import edu.gatech.seclass.sdpvocabquiz.adt.Word
import edu.gatech.seclass.sdpvocabquiz.database.DatabaseManager
import kotlinx.android.synthetic.main.listview_quiz.view.*
import kotlinx.android.synthetic.main.activity_add_words.view.*


class AddWrongListAdapter(private val activity: Activity,
                     private val wrongs: List<String>,
                     internal var txt_wrong: EditText
): BaseAdapter(){

    private val inflater: LayoutInflater

    init{
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.listview_quiz, null)
        rowView.txt_list_sub_element.text = wrongs[position]
        rowView.setOnClickListener { txt_wrong.setText(wrongs[position]) }
        return rowView
    }

    override fun getItem(position: Int): String {
        return wrongs[position]
    }

    override fun getItemId(position: Int): Long {
        return wrongs[position].hashCode().toLong()
    }

    override fun getCount(): Int {
        return wrongs.size
    }

}
