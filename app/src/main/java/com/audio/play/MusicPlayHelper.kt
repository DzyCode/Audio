package com.audio.play

import android.os.SystemClock
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.audio.model.Song
import com.audio.model.convertToMediaDescriptionCompat
import com.audio.util.to
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer

fun <T, E> MutableList<T>.index(e : E, equal : (T, E) -> Boolean) : Int{
    for(i in 0 until size) {
        if(equal(get(i), e)) {
            return i
        }
    }
    return 0
}
fun <T, E> MutableList<T>.at(e : E, equal : (T,E) -> Boolean) : T? {
    for(t in this) {
        if(equal.invoke(t, e)) {
            return t
        }
    }
    return null
}
fun Song.convertToQueueItem(id : String) : MediaSessionCompat.QueueItem {
    return MediaSessionCompat.QueueItem(
            this.convertToMediaDescriptionCompat(id, this._id.toString()),this._id.toLong())
}

fun MediaSessionCompat.QueueItem.convertToSong(id: String) : Song?{
    return description.extras?.getSerializable(id)?.to<Song>()
}

fun SimpleExoPlayer.state() : PlaybackStateCompat {
    var state : Int = PlaybackStateCompat.STATE_NONE
    when(playbackState) {
        ExoPlayer.STATE_IDLE -> state = PlaybackStateCompat.STATE_PAUSED
        ExoPlayer.STATE_BUFFERING -> state = PlaybackStateCompat.STATE_PAUSED
        ExoPlayer.STATE_READY ->
            state = if(playWhenReady) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED
        ExoPlayer.STATE_ENDED -> state = PlaybackStateCompat.STATE_SKIPPING_TO_NEXT
    }
    return PlaybackStateCompat.Builder().setState(state,
            currentPosition, 1.0f, SystemClock.elapsedRealtime()).build()
}

