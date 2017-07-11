package com.audio.present.base

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import com.audio.model.Song

interface IPlayControlCallback{
    fun play() {}
    fun play(mediaId: String, extra: Bundle) {}
    fun pause() {}
    fun playNext() {}
    fun playPre() {}
    fun getPlaybackState() : PlaybackStateCompat?{return null}
}