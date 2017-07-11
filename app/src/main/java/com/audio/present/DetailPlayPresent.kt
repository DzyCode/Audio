package com.audio.present

import android.app.Activity
import com.audio.present.base.Present
import com.audio.util.ChoreographerCompat

class DetailPlayPresent : Present {
    constructor(ctx: Activity) : super(ctx)

    fun registerProgressCallback(callback: (time: Long) -> Unit) {
        ChoreographerCompat.instance.registerCallback(callback)
    }

    fun unRegisterProgressCallback(callback: (time: Long) -> Unit) {
        ChoreographerCompat.instance.unRegisterCallback(callback)
    }
}

