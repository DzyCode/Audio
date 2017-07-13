package com.audio

import android.app.Application
import com.audio.util.NotNullSingleValue

class AudioApp : Application() {
    companion object {
        var instance by NotNullSingleValue<AudioApp>()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

