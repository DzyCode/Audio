package com.audio.view.widget

import android.content.Context
import android.widget.ImageView
import audio.com.audio.R
import com.audio.present.base.IPlayControlCallback
import com.audio.view.layout.selector
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk15.coroutines.onClick
import kotlin.properties.Delegates

class DetailPlayConrolView : _RelativeLayout {
    private var playOrPause: ImageView by Delegates.notNull()
    private var playNext: ImageView by Delegates.notNull()
    private var playPrevience: ImageView by Delegates.notNull()
    private var playList: ImageView by Delegates.notNull()
    private var playModel: ImageView by Delegates.notNull()
    private var isPlaying = false
    private var playControlCallback: IPlayControlCallback? = null

    constructor(ctx: Context) : super(ctx) {
        this.relativeLayout {
            playOrPause = imageView {
                id = R.id.play_pause_btn
                selector(R.mipmap.play_btn_play, R.mipmap.play_btn_play_prs)
                onClick {
                    playOrPauseClick()
                }
            }.lparams(wrapContent, wrapContent) {
                centerInParent()
            }
            playNext = imageView {
                id = R.id.play_next_btn
                selector(R.mipmap.play_btn_next, R.mipmap.play_btn_next_prs)
                onClick { playNextClick() }
            }.lparams(wrapContent, wrapContent) {
                centerVertically()
                rightOf(playOrPause)
            }
            playPrevience = imageView {
                id = R.id.play_previence_btn
                selector(R.mipmap.play_btn_prev, R.mipmap.play_btn_prev_prs)
                onClick { playPreClick() }
            }.lparams(wrapContent, wrapContent) {
                centerVertically()
                leftOf(playOrPause)
            }
            playList = imageView {
                setImageResource(R.mipmap.play_icn_src_prs)
                onClick { playListClick() }
            }.lparams(wrapContent, wrapContent) {
                centerVertically()
                rightOf(playNext)
                setMargins(dip(5), 0, 0, 0)
            }
            playModel = imageView {
                onClick { playModelClick() }
            }.lparams(wrapContent, wrapContent) {
                centerVertically()
                leftOf(playPrevience)
                setMargins(0, 0, dip(5), 0)
            }
        }.lparams(matchParent, wrapContent)
    }

    private fun updatePlayOrPauseBackground() {
        if (!isPlaying) {
            playOrPause.selector(R.mipmap.play_btn_play, R.mipmap.play_btn_play_prs)
        } else {
            playOrPause.selector(R.mipmap.play_btn_pause, R.mipmap.play_btn_pause_prs)
        }
    }

    private fun playOrPauseClick() {
        playControlCallback?.let {
            if(isPlaying) it.pause() else it.play()
            changePlayState()
            updatePlayOrPauseBackground()
        }
    }

    private fun playNextClick() {
        playControlCallback?.playNext()
    }

    private fun playPreClick() {
        playControlCallback?.playPre()
    }

    private fun playListClick() {
    }

    private fun playModelClick() {
    }

    fun registerPlayControlListener(listener: IPlayControlCallback) {
        this.playControlCallback = listener
    }

    fun updatePlayState(isPlay: Boolean) {
        this.isPlaying = isPlay
        updatePlayOrPauseBackground()
    }

    private fun changePlayState() {
        isPlaying = !isPlaying
    }
}
