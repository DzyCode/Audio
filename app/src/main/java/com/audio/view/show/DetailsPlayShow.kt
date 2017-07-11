package com.audio.view.show

import android.app.Activity
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.widget.Toolbar
import android.view.View
import audio.com.audio.R
import com.audio.model.Song
import com.audio.present.DetailPlayPresent
import com.audio.present.base.ICallBack
import com.audio.present.base.IPlayControlCallback
import com.audio.util.*
import com.audio.view.AudioActivity
import com.audio.view.layout.DetailAtyLayout
import com.audio.view.life.AtyLife
import com.audio.view.life.dataToken
import com.audio.view.life.playStateToken
import com.audio.view.widget.DetailPlayConrolView
import com.audio.view.widget.DetailPlayProgressView
import com.audio.view.widget.DetailPlayShowView
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import kotlin.properties.Delegates

class DetailsPlayShow : AtyLife, ICallBack, IPlayControlCallback {
    private var parentView by Delegates.notNull<View>()
    private val toolBar: Toolbar by lazy { parentView.find<Toolbar>(R.id.play_head) }
    private val playControlView by lazy { parentView.find<DetailPlayConrolView>(R.id.play_controll_view) }
    private val playProgressView by lazy { parentView.find<DetailPlayProgressView>(R.id.play_progress_view) }
    private val playShowView by lazy { parentView.find<DetailPlayShowView>(R.id.play_show) }

    private lateinit var present: DetailPlayPresent
    private val progressCallback: (Long) -> Unit = {
        present.getPlaybackState()?.let {
            it.currentPlayProgress().let {
                playProgressView.updateProgress(it)
                playShowView.updateAlbumAngle(it)
            }

        }
    }

    override fun receive(): (Activity, LifeOrder, Any?) -> Any {
        return {
            activity, lifeOrder, any ->
            when (lifeOrder) {
                LifeOrder.ONCREATE -> onCreate(activity)
                LifeOrder.ONRESUME -> onResume()
                LifeOrder.ONSTOP -> onStop()
                LifeOrder.ONDESTROY -> onDestroy()
            }
        }
    }

    private fun onCreate(activity: Activity) {
        initVariable(activity)
        initView(activity)
        initListener()
        start()
    }

    private fun initView(activity: Activity) {
        parentView = DetailAtyLayout().setContentView(activity)
        activity.to<AudioActivity>().let {
            it.setSupportActionBar(toolBar)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        var state = activity.intent
                .getBundleExtra(activity.dataToken())
                .getParcelable<PlaybackStateCompat>(activity.playStateToken())
        present.currentSong()?.let {
            playProgressView.setTotalMs(it.duration)
            state?.let { playProgressView.updateProgress(it.currentPlayProgress()) }
        }
        initPlayShowView(state)
        updatePlayState(state)
        toolBar.bind(present.currentSong())
    }

    private fun initVariable(activity: Activity) {
        present = DetailPlayPresent(activity)
        present.registerCallback(this)
        present.connect()
    }

    private fun initListener() {
        playControlView.registerPlayControlListener(this)
        playShowView.registerPresent(this)
    }

    private fun start() {
    }

    private fun onResume() {
    }

    private fun onStop() {
    }

    private fun onDestroy() {
        present.disconnect()
    }

    private fun initPlayShowView(state: PlaybackStateCompat?) {
        state?.let { playShowView.updatePlayState(it.state == PlaybackStateCompat.STATE_PLAYING) }
    }

    private fun updatePlayState(state: PlaybackStateCompat?) {
        state?.let {
            it.playing {
                playControlView.updatePlayState(true)
                present.registerProgressCallback(progressCallback)
                toolBar.bind(present.currentSong())
            }
            it.paused {
                playControlView.updatePlayState(false)
                present.unRegisterProgressCallback(progressCallback)
            }
        }
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
        updatePlayState(state)
    }

    fun Toolbar.bind(song: Song?) {
        song?.let {
            this.setTitle(it.title)
            this.setSubtitle(it.artist)
        }
    }

    override fun playNext() {
        present.playNext()
        playShowView.playNext()
    }

    override fun playPre() {
        present.playPre()
        playShowView.playPre()
    }

    override fun play() {
        present.play()
        playShowView.play()
    }

    override fun pause() {
        present.pause()
        playShowView.pause()
    }

    override fun getPlaybackState(): PlaybackStateCompat? {
        return present.getPlaybackState()
    }
}
