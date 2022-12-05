package com.code.tusome

import android.app.Application
import com.code.tusome.di.DaggerRoomComponent
import com.code.tusome.di.RoomComponent
import com.code.tusome.di.RoomModule
import com.google.firebase.database.FirebaseDatabase

class Tusome:Application() {
    private lateinit var roomComponent:RoomComponent
    override fun onCreate() {
        super.onCreate()
        roomComponent = DaggerRoomComponent.builder()
            .roomModule(RoomModule(this))
            .build()
    }
    fun getRoomComponent():RoomComponent = roomComponent
}