package com.audio.present.base

import android.support.v4.media.session.PlaybackStateCompat

interface PlayBackCallback {
    fun onPlayStateChanged(state : PlaybackStateCompat) {}
}

