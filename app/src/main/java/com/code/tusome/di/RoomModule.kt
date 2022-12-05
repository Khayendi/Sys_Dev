package com.code.tusome.di

import android.app.Application
import android.content.Context
import com.code.tusome.db.TusomeDB
import com.code.tusome.db.TusomeDao
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(private val application: Application) {
    @Singleton
    @Provides
    fun getRoomDao(tusomeDB: TusomeDB): TusomeDao = tusomeDB.getTusomeDao()

    @Singleton
    @Provides
    fun getRoomInstance(): TusomeDB {
        return TusomeDB.getInstance(provideContext())
    }

    @Singleton
    @Provides
    fun provideContext(): Context = application.applicationContext

//    @Singleton
//    @Provides
//    fun provideFirebaseDatabase(child: String): FirebaseDatabase =
//        FirebaseDatabase.getInstance(child)
}