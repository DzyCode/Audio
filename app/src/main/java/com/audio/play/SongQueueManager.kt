package com.audio.play

import android.support.v4.media.session.MediaSessionCompat
import com.audio.model.Song
import com.audio.util.NotNullReadOnlySingleValue

class SongQueueManager : MusicQueueManager() {

    companion object {
        val instance: SongQueueManager by NotNullReadOnlySingleValue<SongQueueManager>({
            SongQueueManager()
        })
    }

    fun setSongQueue(title: String, songs: MutableList<Song>, initialId: String?) {
        var queue = mutableListOf<MediaSessionCompat.QueueItem>()
        for (song in songs) {
            queue.add(song.convertToQueueItem(song._id.toString()))
        }
        super.setQueue(title, queue, initialId)
    }

    fun getCurrentSong(): Song? {
        var item = getCurrentItem()
        return item?.convertToSong(item.description.mediaId!!)
    }

    fun getNextSong(): Song? {
        var item = getItemSkip(1)
        return item?.convertToSong(item.description.mediaId!!)
    }

    fun getPreSong(): Song? {
        var item = getItemSkip(-1)
        return item?.convertToSong(item.description.mediaId!!)
    }

    fun getSongWithId(mediaId: String): Song? {
        return getItemWithId(mediaId)?.convertToSong(mediaId)
    }
}

