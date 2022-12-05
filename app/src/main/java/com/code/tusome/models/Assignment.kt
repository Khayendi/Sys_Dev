package com.code.tusome.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Assignment(
    val uid: String,
    val unitName: String,
    val name: String,
    val issueDate: Long,
    val dueDate: Long
)

@Entity(tableName = "assignment")
data class AssignmentDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "unit_name") val unitName: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date_issued") val issueDate: Long,
    @ColumnInfo(name = "due_date") val dueDate: Long
)
