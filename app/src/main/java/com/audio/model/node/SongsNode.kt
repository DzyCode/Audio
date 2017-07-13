package com.audio.model.node

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.audio.model.loadLocalSongs
import com.audio.model.loadSongHead

class SongsNode : Node() {
    override fun load(context: Context, parentId: String, load: (MutableList<MediaBrowserCompat.MediaItem>) -> Unit) {
        val result = mutableListOf<MediaBrowserCompat.MediaItem>()
        result.addAll(context.loadSongHead(parentId))
        result.addAll(context.loadLocalSongs(parentId))
        load.invoke(result)
    }
}

