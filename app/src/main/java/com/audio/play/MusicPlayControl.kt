package com.audio.play

import android.app.Activity
import android.os.Bundle
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.audio.model.Song
import kotlin.properties.Delegates

class MusicPlayControl {
    var context: Activity by Delegates.notNull<Activity>()
    var mediaControll : MediaControllerCompat? = null
        get() {
            return if(field == null) MediaControllerCompat.getMediaController(context) else field
        }
    constructor(activity: Activity) {
        this.context = activity
    }

    fun getPlaybackState(): PlaybackStateCompat? {
        return mediaControll?.playbackState
    }

    fun play() {
        mediaControll?.transportControls?.play()
    }

    fun play(song: Song?) {
        song?.let {
            mediaControll?.transportControls?.playFromMediaId(it._id.toString(), null)
        }
    }

    fun playForMediaId(mediaId: String, extra: Bundle) {
        mediaControll?.transportControls?.playFromMediaId(mediaId, extra)
    }

    fun pause() {
        mediaControll?.transportControls?.pause()
    }

    fun playNext() {
        mediaControll?.transportControls?.skipToNext()
    }

    fun playPrevience() {
        mediaControll?.transportControls?.skipToPrevious()
    }

    fun queueTitle(): String? {
        return mediaControll?.queueTitle?.toString()
    }
}

