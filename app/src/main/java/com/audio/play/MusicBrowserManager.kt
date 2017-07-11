package com.audio.play

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import com.audio.util.log
import com.audio.util.to

class MusicBrowserManager {
    var mediaBrowser: MediaBrowserCompat
    var context: Context
    var callback: MediaControllerCompat.Callback? = null

    constructor(context: Context) {
        mediaBrowser = MediaBrowserCompat(context, ComponentName(context, MusicService::class.java),
                ConnectionCallback(), null)
        this.context = context
    }

    fun connect(callback: MediaControllerCompat.Callback?) {
        if (!mediaBrowser.isConnected) {
            mediaBrowser.connect()
            this.callback = callback
        }
    }

    fun disconnect() {
        if (mediaBrowser.isConnected) {
            mediaBrowser.disconnect()
        }
    }

    fun load(id: String,
             load: (String, List<MediaBrowserCompat.MediaItem>) -> Unit,
             error: (String) -> Unit) {
        mediaBrowser.unsubscribe(id)
        mediaBrowser.subscribe(id, SubscribeCallback(load, error))
    }

    fun play() {
//        mediaControll.transportControls.play()
    }

    fun playForMediaId(mediaId: String, extra: Bundle) {
//        mediaControll.transportControls.playFromMediaId(mediaId, extra)
    }

    inner class SubscribeCallback : MediaBrowserCompat.SubscriptionCallback {
        var load: (String, List<MediaBrowserCompat.MediaItem>) -> Unit
        var error: (String) -> Unit

        constructor(load: (String, List<MediaBrowserCompat.MediaItem>) -> Unit,
                    error: (String) -> Unit) : super() {
            this.load = load
            this.error = error
        }

        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            super.onChildrenLoaded(parentId, children)
            load.invoke(parentId, children)
        }

        override fun onError(parentId: String) {
            super.onError(parentId)
            error.invoke(parentId)
        }
    }

    inner class ConnectionCallback : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {//set token 后调用
            var mediaControll = MediaControllerCompat(context, mediaBrowser.sessionToken)
            MediaControllerCompat.setMediaController(context.to<Activity>(), mediaControll)
            mediaControll.registerCallback(callback)
        }

        override fun onConnectionSuspended() {
        }

        override fun onConnectionFailed() {
        }
    }
}
