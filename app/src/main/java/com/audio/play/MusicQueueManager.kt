package com.audio.play

import android.support.v4.media.session.MediaSessionCompat
import com.audio.util.execute

open class MusicQueueManager {

    private var callback = mutableListOf<QueueCallback>()

    protected var playQueue = mutableListOf<MediaSessionCompat.QueueItem>()
    protected var queueTitle: String = ""
    protected var currentIndex = 0

    fun registerCallback(callback: QueueCallback) {
        this.callback.add(callback)
    }

    fun unRegisterCallback(callback: QueueCallback) {
        this.callback.remove(callback)
    }

    fun setQueue(title: String, queue: MutableList<MediaSessionCompat.QueueItem>, initIndex : Int) {
        playQueue = queue
        queueTitle = title
        currentIndex = initIndex
        notifyQueueUpdate()
    }

    fun indexOf(id: String?): Int {
        return playQueue.index(id, { queueItem, s ->
            queueItem.description.mediaId.equals(id)
        })
    }

    fun addQueueItem(queueItem: MediaSessionCompat.QueueItem) {
        playQueue.add(queueItem)
        notifyQueueUpdate()
    }

    fun removeQueueItem(queueItem: MediaSessionCompat.QueueItem) {
        playQueue.remove(queueItem)
        notifyQueueUpdate()
    }

    open fun setCurrentIndex(mediaId: String?) : Int{
        currentIndex = playQueue.index(mediaId, {
            queueItem, id ->
            queueItem.description.mediaId.equals(id)
        })
        return currentIndex
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

    open fun skipQueuePosition(amount: Int){
        val pos = currentIndex + amount
        (pos <= 0).execute({ currentIndex = 0 },{currentIndex = pos % playQueue.size})
    }

    fun canPlayNext(): Boolean {
        return currentIndex < playQueue.size - 1
    }

    fun canPlayPre(): Boolean {
        return currentIndex > 0
    }

    private fun notifyQueueUpdate() {
        callback.forEach {
            it.onQueueUpdate(queueTitle, playQueue)
        }
    }
//    fun setCurrentIndex(index: Int) {
//        if (index >= 0) {
//            currentIndex = index % playQueue.size
//        }
//    }

    interface QueueCallback {
        fun onQueueUpdate(title: String, queue: MutableList<MediaSessionCompat.QueueItem>)
    }
}