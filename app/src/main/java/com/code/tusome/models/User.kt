package com.code.tusome.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
    val uid: String,
    val username: String,
    val email: String,
    val imageUrl: String,
    val role: Role?,
){
    constructor():this(
        "",
        "",
        "",
        "",
        null)
}

@Entity(tableName = "user")
data class UserDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @ColumnInfo(name = "role") val role: String,
)

data class Role(val roleName: String){
    constructor():this("")
}

@Entity(tableName = "role")
data class RoleDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "uid")val uid: String,
    @ColumnInfo(name = "role_name")val roleName: String
)
