package com.audio.present

import android.app.Activity
import com.audio.model.Song
import com.audio.present.base.Present

class DefaultPresent(ctx : Activity,  connected : () -> Unit = {}) : Present(ctx, connected) {
    fun play(song : Song) {
        playControl.play(song)
    }
}