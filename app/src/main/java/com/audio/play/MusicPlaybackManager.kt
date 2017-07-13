package com.audio.play

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import com.audio.model.Song
import com.audio.model.db.RecentlyPlayManager
import com.audio.present.base.PlayBackCallback
import com.audio.util.agent.logd
import com.audio.util.letNotNull
import com.audio.util.toRecentlyPlayList
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class MusicPlaybackManager {
    val medisSessionCallback: MediaSessionCallback
    val context: Context
    var exoplayer: SimpleExoPlayer? = null
    var playbackCallback: PlayBackCallback? = null
    val exoplayerListener by lazy { ExoplayerListener() }

    constructor(context: Context) {
        medisSessionCallback = MediaSessionCallback()
        this.context = context
    }

    fun registerPlayCallback(callback: PlayBackCallback) {
        playbackCallback = callback
    }

    fun play(song: Song?) {
        song?.let {
            RecentlyPlayManager.instance.insertOrUpdate(it.toRecentlyPlayList())
            val audiomanager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audiomanager.requestAudioFocus(
                    null,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN)
            val factory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "audio"), null)
            val mediasource = ExtractorMediaSource(Uri.parse(it.data.replace(" ", "%20")), factory,
                    DefaultExtractorsFactory(), null, null)
            exoplayer()?.let {
                it.prepare(mediasource)
                it.volume = 0.5f
                it.playWhenReady = true
            }
        }
    }

    fun exoplayer(): SimpleExoPlayer? {
        return exoplayer ?: with(this) {
            exoplayer = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
            exoplayer?.let {
                exoplayer!!.audioStreamType = AudioManager.STREAM_MUSIC
                exoplayer!!.addListener(exoplayerListener)
                exoplayer
            }
        }
    }

    fun releaseExoplayer() {
        exoplayer?.let {
            it.release()
            it.removeListener(exoplayerListener)
            exoplayer = null
        }
    }

    inner class MediaSessionCallback : MediaSessionCompat.Callback() {
        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            val song = SongQueueManager.instance.getSongWithId(mediaId ?: "")
            song?.let {
                play(it)
                SongQueueManager.instance.setCurrentIndex(mediaId)
            }
        }

        override fun onPause() {
            exoplayer()?.let {
                it.playWhenReady = false
            }
        }

        override fun onPlay() {
            exoplayer()?.let {
                it.playWhenReady = true
            }
        }

        override fun onStop() {
            exoplayer()?.let {
                it.stop()
                releaseExoplayer()
            }
        }

        override fun onSeekTo(pos: Long) {
            exoplayer()?.seekTo(pos)
        }

        override fun onSkipToNext() {
            SongQueueManager.instance.skipQueuePosition(1)
            play(SongQueueManager.instance.getCurrentSong())
        }

        override fun onSkipToPrevious() {
            SongQueueManager.instance.skipQueuePosition(-1)
            play(SongQueueManager.instance.getCurrentSong())
        }

        override fun onSkipToQueueItem(id: Long) {
            super.onSkipToQueueItem(id)
        }

    }

    inner class ExoplayerListener : ExoPlayer.EventListener {
        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
        }

        override fun onPositionDiscontinuity() {
        }

        override fun onLoadingChanged(isLoading: Boolean) {
        }

        override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
        }

        override fun onPlayerError(error: ExoPlaybackException?) {
        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            exoplayer()?.letNotNull(playbackCallback, {
                logd("PlaybackManager","play state changed: " + it.state())
                this.onPlayStateChanged(it.state())
            })
        }
    }
}