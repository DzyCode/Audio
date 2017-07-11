package com.audio.play

import android.support.v4.media.session.MediaSessionCompat

open class MusicQueueManager {

    private var callback = mutableListOf<QueueCallback>()

    private var playQueue = mutableListOf<MediaSessionCompat.QueueItem>()
    private var currentIndex = 0

    fun registerCallback(callback: QueueCallback) {
        this.callback.add(callback)
    }

    fun unRegisterCallback(callback: QueueCallback) {
        this.callback.remove(callback)
    }

    fun setQueue(title: String, queue: MutableList<MediaSessionCompat.QueueItem>, initialId: String? = null) {
        playQueue = queue
        currentIndex = queue.index(initialId, {
            queueItem, id ->
            queueItem.description.mediaId.equals(initialId)
        })
        callback.forEach {
            it.onQueueUpdate(title, queue)
        }
    }

    fun setCurrentIndex(index: Int) {
        if (index >= 0) {
            currentIndex = index % playQueue.size
        }
    }

    fun setCurrentIndex(mediaId: String?) {
        currentIndex = playQueue.index(mediaId, {
            queueItem, id ->
            queueItem.description.mediaId.equals(id)
        })
    }

    fun getCurrentItem(): MediaSessionCompat.QueueItem? {
        return getItemAt(currentIndex)
    }

    fun getItemSkip(pos: Int): MediaSessionCompat.QueueItem? {
        return getItemAt(currentIndex + pos)
    }

    private fun getItemAt(pos: Int): MediaSessionCompat.QueueItem? {
        return if (pos >= playQueue.size || pos < 0) null else playQueue[pos]
    }

    fun getItemWithId(mediaId: String): MediaSessionCompat.QueueItem? {
        return playQueue.at(mediaId, {
            queueItem, id ->
            queueItem.description.mediaId.equals(id)
        })
    }

    fun skipQueuePosition(amount: Int) {
        var pos = currentIndex + amount
        if (pos <= 0) {
            pos = 0
        } else if (playQueue.size > pos) {
            pos %= playQueue.size
        }
        currentIndex = pos
    }

    fun canPlayNext(): Boolean {
        return currentIndex < playQueue.size - 1
    }

    fun canPlayPre(): Boolean {
        return currentIndex > 0
    }

    interface QueueCallback {
        fun onQueueUpdate(title: String, queue: MutableList<MediaSessionCompat.QueueItem>)
    }
}

