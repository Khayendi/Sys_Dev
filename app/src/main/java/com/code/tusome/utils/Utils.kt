package com.code.tusome.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object Utils {
    fun snackBar(view: View, message:String){
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).apply {
            animationMode = Snackbar.ANIMATION_MODE_FADE
            show()
        }
    }
}