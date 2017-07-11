package com.audio.play

import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.audio.model.MusicProvider
import com.audio.present.base.PlayBackCallback
import com.audio.util.log

class MusicService : MediaBrowserServiceCompat() {

    lateinit var session: MediaSessionCompat
    lateinit var playManager: MusicPlaybackManager
    lateinit var musicProvider: MusicProvider
    lateinit var songQueueManager: SongQueueManager
    override fun onCreate() {
        super.onCreate()
        playManager = MusicPlaybackManager(this)
        playManager.registerPlayCallback(PlayCallback())
        musicProvider = MusicProvider(this)

        session = MediaSessionCompat(this, "MusicSession")
        sessionToken = session.sessionToken
        session.setCallback(playManager.medisSessionCallback)
        session.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

        SongQueueManager.instance.registerCallback(QueueCallback())
    }

    override fun onBind(intent: Intent?): IBinder {
        return super.onBind(intent)
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {//bind service后调用
        return BrowserRoot("id", rootHints)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        musicProvider.registNode(parentId)
        musicProvider.load(parentId, {
            list ->
            result.detach()
            result.sendResult(list)
        })
    }

    inner class QueueCallback : MusicQueueManager.QueueCallback {
        override fun onQueueUpdate(title: String, queue: MutableList<MediaSessionCompat.QueueItem>) {
            session.setQueue(queue)
            session.setQueueTitle(title)
        }
    }

    inner class PlayCallback : PlayBackCallback {
        override fun onPlayStateChanged(state: PlaybackStateCompat) {
            session.setPlaybackState(state)
            log("musicservice", "play state changed: " + state.state)
            when (state.state) {
                PlaybackStateCompat.STATE_SKIPPING_TO_NEXT -> {
                    SongQueueManager.instance.skipQueuePosition(1)
                    playManager.play(SongQueueManager.instance.getCurrentSong())
                }
            }
        }
    }
}