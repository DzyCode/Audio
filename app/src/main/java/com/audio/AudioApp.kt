package com.audio

import android.app.Application
import android.provider.Settings
import android.util.Log
import android.view.Choreographer
import com.audio.util.ChoreographerCompat
import com.audio.util.NotNullSingleValue
import com.audio.util.log

class AudioApp : Application() {
    companion object {
        var instance by NotNullSingleValue<AudioApp>()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

