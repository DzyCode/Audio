package com.audio.present

import android.app.Activity
import com.audio.model.Song
import com.audio.play.MusicPlayControl
import com.audio.present.base.Present

class RecentlyPlayPresent : Present {

    val musicPlayControl : MusicPlayControl
    constructor(context : Activity) : super(context) {
        musicPlayControl = MusicPlayControl(context)
    }

    fun play(song: Song) {
        musicPlayControl.play(song)
    }




}

