package com.code.tusome.db

import androidx.room.TypeConverter
import com.code.tusome.models.Role

object Converter {
    @TypeConverter
    fun fromRoleToString(role: Role): String = role.roleName
}