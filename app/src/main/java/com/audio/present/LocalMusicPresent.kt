package com.audio.present

import android.app.Activity
import com.audio.play.MusicPlayControl
import com.audio.present.base.Present

class LocalMusicPresent : Present {
    val musicPlayControl : MusicPlayControl
    constructor(context: Activity) : super(context) {
        musicPlayControl = MusicPlayControl(context)
    }
}

