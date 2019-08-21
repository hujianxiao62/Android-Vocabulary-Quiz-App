package edu.gatech.seclass.sdpvocabquiz.adt

import java.io.Serializable

data class Student(val username: String, val major: String = "", val seniorityLevel: String = "", val emailAddress: String = ""): Serializable {

}