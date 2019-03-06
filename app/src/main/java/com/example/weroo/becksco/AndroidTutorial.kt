package com.example.weroo.becksco

import android.app.Application
import com.example.weroo.becksco.api.DefaultPrefHelper

class AndroidTutorial: Application() {

    override fun onCreate() {
        super.onCreate()
        DefaultPrefHelper.init(this)
    }
}