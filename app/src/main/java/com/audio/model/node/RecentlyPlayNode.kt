package com.audio.model.node

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.audio.model.getRecentlyPlayList
import com.audio.model.loadRecentlyPlayHeader

class RecentlyPlayNode : Node() {
    override fun load(context: Context, parentId: String, load: (MutableList<MediaBrowserCompat.MediaItem>) -> Unit) {
        val result = context.loadRecentlyPlayHeader(parentId)
        result.addAll(context.getRecentlyPlayList(parentId))
        load.invoke(result)
    }
}

