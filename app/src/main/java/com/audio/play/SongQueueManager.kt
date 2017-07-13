package com.audio.play

import android.support.v4.media.session.MediaSessionCompat
import com.audio.model.Song
import com.audio.util.NotNullReadOnlySingleValue
import com.audio.util.agent.logd
import com.audio.util.execute
import com.audio.util.filter
import com.audio.util.yes

class SongQueueManager : MusicQueueManager() {
    private var songToDo: Song? = null

    companion object {
        val instance: SongQueueManager by NotNullReadOnlySingleValue({
            SongQueueManager()
        })
    }

    fun setSongQueue(title: String, songs: MutableList<Song>, initialId: String?) {
        logd(this, "set song queue with title[$queueTitle -> $title]")
        (queueTitle == title).execute({
            updateSongQueue(title, songs, initialId)
        }, {
            queueTitle = title
            songToDo = null
            super.setQueue(title, songsToQueueItem(songs), songIndex(initialId, songs))
        })
    }

    fun updateSongQueue(title: String, songs: MutableList<Song>, initialId: String?) {
        (oldQueueContains(initialId) && !queueContains(initialId, songs)).execute({
            val oldIndex = indexOf(initialId)
            logd(this,"update song queue oldIndex -> currentIndex [$oldIndex  $currentIndex]")
            logd(this,"update song queue with: $initialId  -> $songs")
            (oldIndex == currentIndex).execute({
                //ensure current play song  is initial song
                songToDo = playQueue[currentIndex].convertToSong(initialId!!)
                setQueue(title, songsToQueueItem(songs), updateInitialIndex(oldIndex, songs))
            }, {
                songToDo = null
                setQueue(title, songsToQueueItem(songs), songIndex(initialId, songs))
            })
        }, {
            logd(this,"update song queue and set todo null")
            songToDo = null
            super.setQueue(title, songsToQueueItem(songs), songIndex(initialId, songs))
        })
    }

    private fun songsToQueueItem(songs: MutableList<Song>): MutableList<MediaSessionCompat.QueueItem> {
        return songs.filter { it.convertToQueueItem(it._id.toString()) }
    }

    private fun oldQueueContains(id: String?): Boolean {
        return playQueue.size > 0 && playQueue[indexOf(id)].description.mediaId.equals(id)
    }

    private fun queueContains(id: String?, songs: MutableList<Song>): Boolean {
        val index = songIndex(id, songs)
        return songs.size > 0 && songs[index]._id.toString() == id
    }

    private fun songIndex(id: String?, songs: MutableList<Song>): Int {
        return songs.index(id, { (_id), s -> _id.toString() == id })
    }

    private fun updateInitialIndex(oldIndex: Int, songs: MutableList<Song>): Int {
        for (i in oldIndex until playQueue.size) {
            val id = playQueue[i].description.mediaId
            val newIndex = songIndex(id, songs)
            if (songs[newIndex]._id.toString() == id) {
                return newIndex
            }
        }
        return 0
    }

    fun getCurrentSong(): Song? {
        return songToDo ?: currentSong()
    }

    private fun currentSong(): Song? {
        return getCurrentItem()?.convertSelfToSong()
    }

    fun getNextSong(): Song? {
        logd(this,"get next song  with todo:[$songToDo]")
        val skip = if (songToDo == null) 1 else 0
        return getItemSkip(skip)?.convertSelfToSong()
    }

    fun getPreSong(): Song? {
        logd(this, "get pre song with todo: [$songToDo]")
        return getItemSkip(-1)?.convertSelfToSong()
    }

    fun getSongWithId(mediaId: String): Song? {
        return getItemWithId(mediaId)?.convertToSong(mediaId)
    }

    override fun setCurrentIndex(mediaId: String?) : Int{
        val current = currentIndex
        (current != super.setCurrentIndex(mediaId)).yes { songToDo = null }
        return currentIndex
    }

    override fun skipQueuePosition(amount: Int){
        logd(this,"skip position before [$songToDo  ->  [$currentIndex   $amount]")
        (amount != 0).yes {
            (songToDo == null).execute({
               super.skipQueuePosition(amount)
            },{
                val skip = if(amount > 0) -1 else 0
                super.skipQueuePosition(amount + skip)
                songToDo = null
            })
        }
        logd(this,"skip position after [$songToDo  ->  [$currentIndex   $amount]")
    }
}

