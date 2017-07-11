package com.audio.util

import android.view.Choreographer
import java.util.concurrent.CopyOnWriteArrayList

class ChoreographerCompat {
    private val choreographer: Choreographer
    private var doFrame = CopyOnWriteArrayList<(time: Long) -> Unit>()
    private var isIdeal = true

    companion object {
        var instance = ChoreographerCompat()
    }

    private constructor() {
        choreographer = Choreographer.getInstance()
    }

    fun registerCallback(callback: (time: Long) -> Unit) {
        doFrame.add(callback)
        startIfIdeal()
    }

    fun unRegisterCallback(callback: (time: Long) -> Unit) {
        doFrame.remove(callback)
    }

    private fun startIfIdeal() {
        if (isIdeal) {
            choreographer.postFrameCallback(FrameCallback())
            isIdeal = false
        }
    }

    inner class FrameCallback : Choreographer.FrameCallback {
        override fun doFrame(p0: Long) {
            doFrame.forEach { it.invoke(p0) }
            if (doFrame.size == 0) {
                isIdeal = true
            } else {
                choreographer.postFrameCallback(this)
            }
        }
    }
}