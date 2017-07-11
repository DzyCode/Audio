package com.audio.view.widget

import android.content.Context
import android.widget.SeekBar
import android.widget.TextView
import audio.com.audio.R
import com.audio.util.MMSS
import com.audio.util.log
import org.jetbrains.anko.*
import kotlin.properties.Delegates

class DetailPlayProgressView : _RelativeLayout {

    private var tProgress by Delegates.notNull<TextView>()
    private var eProgress by Delegates.notNull<TextView>()
    private var seekBar by Delegates.notNull<SeekBar>()

    constructor(ctx: Context) : super(ctx) {
        relativeLayout {
            tProgress = textView {
                id = R.id.progress_timing
                setTextColor(R.color.primary_dark_material_dark)
            }.lparams(wrapContent, wrapContent) {
                setMargins(dip(10), 0, dip(3), 0)
                alignParentLeft()
                centerVertically()
            }
            eProgress = textView {
                id = R.id.progress_end
                setTextColor(R.color.primary_dark_material_dark)
            }.lparams(wrapContent, wrapContent) {
                setMargins(dip(3), 0, dip(10), 0)
                alignParentRight()
                centerVertically()
            }
            seekBar = seekBar {

            }.lparams(matchParent, wrapContent) {
                rightOf(tProgress)
                leftOf(eProgress)
                centerVertically()
            }
        }.lparams(matchParent, wrapContent)
    }

    fun setTotalMs(time: Long) {
        seekBar.max = time.toInt()
        eProgress.text = time.MMSS()
    }

    fun updateProgress(time: Long) {
        tProgress.text = time.MMSS()
        seekBar.progress = Math.min(time.toInt(), seekBar.max)
    }
}