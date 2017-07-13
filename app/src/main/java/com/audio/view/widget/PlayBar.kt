package com.audio.view.widget

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import audio.com.audio.R
import com.audio.AudioApp
import com.audio.model.Song
import com.audio.model.picUri
import com.audio.play.MusicPlayControl
import com.audio.util.agent.startActivity
import com.audio.util.load
import com.audio.view.life.Show
import com.audio.view.life.playStateToken
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk15.coroutines.onClick


class PlayBar(parent: View, context : Activity) {
    val parentView by lazy { parent.find<RelativeLayout>(R.id.audio_navigation_bar) }
    val playIcon by lazy { parent.find<ImageView>(R.id.audio_navigation_icon) }
    val playTitle by lazy { parent.find<TextView>(R.id.audio_navigation_title) }
    val playSubTitle by lazy { parent.find<TextView>(R.id.audio_navigation_subtitle) }
    val playList by lazy { parent.find<ImageView>(R.id.audio_navigation_playlist) }
    val playOrPause by lazy { parent.find<ImageView>(R.id.audio_navigation_play) }
    val playNext by lazy { parent.find<ImageView>(R.id.audio_navigation_next) }
    val playControl by lazy { MusicPlayControl(context) }

    companion object {
        var isPlay = false
        var song: Song? = null
    }

    fun play(song: Song?) {
        Companion.song = song
        isPlay = true
        update()
        playOrPause.onClick { if (isPlay) playControl.pause() else playControl.play() }
        playNext.onClick { playControl.playNext() }
        parentView.onClick {
            val bundle = Bundle()
            bundle.putParcelable(AudioApp.instance.playStateToken(), playControl.getPlaybackState())
            parentView.context.startActivity(Show.DETAILSPLAYSHOW, bundle)
        }
    }

    fun pause() {
        isPlay = false
        update()
    }

    fun update() {
        parentView.visibility = if (song == null) View.GONE else View.VISIBLE
        song?.let {
            playTitle.text = it.title
            playSubTitle.text = it.artist + " - " + it.album
            playIcon.load(it.picUri())
            val resId = if (isPlay) R.mipmap.playbar_btn_pause else R.mipmap.playbar_btn_play
            playOrPause.setImageResource(resId)
        }
    }
}

