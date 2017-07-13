package com.audio.present.base

import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat

interface ICallBack {
    fun onAudioInfoChanged(info: MediaControllerCompat.PlaybackInfo?) {
    }

    fun onMetadataChanged(metadata: MediaMetadataCompat?) {
    }

    fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) {
//        onSongQueueChanged(queue?.filter { it.convertSelfToSong() })
    }

//    fun onSongQueueChanged(songs: MutableList<Song>?) {
//
//    }

    fun onQueueTitleChanged(title: CharSequence?) {
    }

    fun onExtrasChanged(extras: Bundle?) {
    }

    fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
    }

    fun onSessionDestroyed() {
    }

    fun onSessionEvent(event: String?, extras: Bundle?) {
    }

    fun onRepeatModeChanged(repeatMode: Int) {
    }

    fun onShuffleModeChanged(enabled: Boolean) {
    }

}

