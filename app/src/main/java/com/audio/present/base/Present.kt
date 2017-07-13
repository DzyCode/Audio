package com.audio.present.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.audio.model.Song
import com.audio.play.MusicBrowserManager
import com.audio.play.MusicPlayControl
import com.audio.play.SongQueueManager

abstract class Present : IPlayControlCallback {
    protected val musicBrowserManager: MusicBrowserManager
    protected val playControl: MusicPlayControl
    private var callBack: ICallBack? = null
    protected val context: Context

    constructor(context: Activity, connected : () -> Unit = {}) {
        musicBrowserManager = MusicBrowserManager(context, connected)
        playControl = MusicPlayControl(context)
        this.context = context
    }

    fun registerCallback(callback: ICallBack) {
        this.callBack = callback
    }

    fun connect() {
        musicBrowserManager.connect(Callback())
    }

    fun disconnect() {
        musicBrowserManager.disconnect()
    }

    fun <T> loadDataWithId(id: String, load: (String, List<T>) -> Unit) {
        musicBrowserManager.load(id, {
            s, list ->
            val result = mutableListOf<T>()
            list.map { it.description.extras }
                    .mapTo(result){it?.getSerializable(id) as T}
            load.invoke(id, result)
        }, {
            s ->
        })
    }

    override fun play() {
        playControl.play()
    }

    override fun play(mediaId: String, extra: Bundle) {
        playControl.playForMediaId(mediaId, extra)
    }

    override fun pause() {
        playControl.pause()
    }

    override fun playNext() {
        playControl.playNext()
    }

    override fun playPre() {
        playControl.playPrevience()
    }

    fun currentSong(): Song? {
        return SongQueueManager.instance.getCurrentSong()
    }

    fun queueTitle(): String? {
        return playControl.queueTitle()
    }

    override fun getPlaybackState(): PlaybackStateCompat? {
        return playControl.getPlaybackState()
    }

    inner class Callback : MediaControllerCompat.Callback() {
        override fun onAudioInfoChanged(info: MediaControllerCompat.PlaybackInfo?) {
            super.onAudioInfoChanged(info)
        }

        override fun onShuffleModeChanged(enabled: Boolean) {
            super.onShuffleModeChanged(enabled)
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            super.onRepeatModeChanged(repeatMode)
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
        }

        override fun onExtrasChanged(extras: Bundle?) {
            super.onExtrasChanged(extras)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            callBack?.onPlaybackStateChanged(state)
        }

        override fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) {
            callBack?.onQueueChanged(queue)
        }

        override fun onQueueTitleChanged(title: CharSequence?) {
            super.onQueueTitleChanged(title)
        }
    }
}

