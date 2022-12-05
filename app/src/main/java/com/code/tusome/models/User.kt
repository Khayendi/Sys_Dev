package com.code.tusome.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserModel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String
)

data class User(
    val uid: String,
    val username: String,
    val email: String,
    val imageUrl: String,
    val role: Role,
    val isAdmin: Boolean
)

data class Role(val uid: String, val roleName: String)
