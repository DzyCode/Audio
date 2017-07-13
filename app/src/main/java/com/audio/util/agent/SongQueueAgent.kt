package com.audio.util.agent

import com.audio.model.Song
import com.audio.model.db.CollectionPlayManager
import com.audio.play.SongQueueManager


fun <T : Any> T.canPlayNext(): Boolean {
    return SongQueueManager.instance.canPlayNext()
}

fun <T : Any> T.canPlayPre(): Boolean {
    return SongQueueManager.instance.canPlayPre()
}

fun <T : Any> T.currentSong(): Song? {
    return SongQueueManager.instance.getCurrentSong()
}

fun <T : Any> T.nextSong(): Song? {
    return SongQueueManager.instance.getNextSong()
}

fun <T : Any> T.preSong(): Song? {
    return SongQueueManager.instance.getPreSong()
}

fun <T : Any> T.markCurrentSongToCollection() {
    SongQueueManager.instance.currentSong()?.let {
        logd(this.javaClass.name, "mark collection with song[${it.title}  ${it._id}]")
        markCollection(it)

    }
}

fun <T : Any> T.unMarkCurrentSongToCollection() {
    SongQueueManager.instance.currentSong()?.let {
        logd(this.javaClass.name, "unmark collection with song[${it.title}  ${it._id}]")
        unMarkCollection(it)
    }
}

fun <T : Any> T.markCollection(song: Song) {
    CollectionPlayManager.instance.insertSong(song._id)
}

fun <T : Any> T.unMarkCollection(song: Song) {
    CollectionPlayManager.instance.deleteSong(song._id)
}

fun <T : Any> T.isMarkedSongToCollection(songId: Int): Boolean {
    return CollectionPlayManager.instance.getSongCollection().contains(songId)
}

fun <T : Any> T.setSongQueue(title: String, songs: MutableList<Song>, initialId: String?) {
    SongQueueManager.instance.setSongQueue(title, songs, initialId)
}