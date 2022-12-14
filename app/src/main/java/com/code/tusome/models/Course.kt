package com.code.tusome.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Course(
    val uid: String,
    val courseCode: String,
    val units: ArrayList<CourseUnit>,
    val department: String,
    val school: String
) {
    constructor() : this("", "", ArrayList(), "", "")
}

@Entity(tableName = "course")
data class CourseDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "course_code") val courseCode: String,
    @ColumnInfo(name = "units") val units: ArrayList<CourseUnit>,
    @ColumnInfo(name = "department") val department: String,
    @ColumnInfo(name = "school") val school: String
)

data class CourseUnit(
    val uid: String,
    val instructor: String,
    val year: String,
    val exam: ArrayList<Exam>,
    val cat: ArrayList<Cat>
) {
    constructor() : this("", "", "", ArrayList(), ArrayList())
}

@Entity(tableName = "unit")
data class CourseUnitDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "instructor") val instructor: String,
    @ColumnInfo(name = "year") val year: String,
    @ColumnInfo(name = "exam") val exam: ArrayList<Exam>,
    @ColumnInfo(name = "cats") val cat: ArrayList<Cat>
)

data class Cat(
    val uid: String,
    val unitName: String,
    val description: String,
    val courseCode: String,
    val date: String,
    val duration: String,
    val invigilator: String
) {
    constructor() : this("", "", "", "", "", "", "")
}

@Entity(tableName = "cat")
data class CatDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "invigilator") val invigilator: String,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "room") val room: String,
    @ColumnInfo(name = "duration") val duration: String
)

data class Exam(
    val uid: String,
    val unitName: String,
    val description: String,
    val courseCode: String,
    val date: String,
    val duration: String,
    val invigilator: String
) {
    constructor() : this("", "", "", "", "", "", "")
}

@Entity(tableName = "exam")
data class ExamDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "unit_name") val unitName: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "course_code") val courseCode: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "duration") val duration: String,
    @ColumnInfo(name = "invigilator") val invigilator: String
)


data class Assignment(
    val uid: String,
    val unitName: String,
    val description: String,
    val issueDate: String,
    val dueDate: String,
) {
    constructor() : this("", "", "", "", "")

}

@Entity(tableName = "assignment")
data class AssignmentDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "unit_name") val unitName: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date_issued") val issueDate: String,
    @ColumnInfo(name = "due_date") val dueDate: String
)