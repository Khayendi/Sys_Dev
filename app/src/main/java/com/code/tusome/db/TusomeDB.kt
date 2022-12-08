package com.code.tusome.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.code.tusome.models.AssignmentDB
import com.code.tusome.models.UserDB

@Database(entities = [UserDB::class,AssignmentDB::class], version = 1, exportSchema = false)
@TypeConverters(value = [Converter::class])
abstract class TusomeDB : RoomDatabase() {
    abstract fun getTusomeDao():TusomeDao
    companion object {
        private var instance: TusomeDB? = null
        fun getInstance(context: Context): TusomeDB {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    TusomeDB::class.java, "TusomeDB"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}