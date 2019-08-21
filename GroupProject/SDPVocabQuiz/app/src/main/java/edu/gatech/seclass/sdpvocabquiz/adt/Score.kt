package edu.gatech.seclass.sdpvocabquiz.adt

import java.io.Serializable

data class Score(val student: Student, val quiz: Quiz, val percentage: Double, val date: Long): Serializable {

}