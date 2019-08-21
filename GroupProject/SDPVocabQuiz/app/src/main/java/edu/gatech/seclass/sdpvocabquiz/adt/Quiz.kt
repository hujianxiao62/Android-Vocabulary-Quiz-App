package edu.gatech.seclass.sdpvocabquiz.adt

import java.io.Serializable

data class Quiz(val owner: Student, val name: String, val description: String, val words: List<Word>, val definitions: List<String>) : Serializable {

}