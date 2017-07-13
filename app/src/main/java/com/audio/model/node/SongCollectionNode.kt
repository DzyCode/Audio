package com.audio.model.node

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.audio.model.loadCollections


class SongCollectionNode : Node() {
    override fun load(context: Context, parentId: String, load: (MutableList<MediaBrowserCompat.MediaItem>) -> Unit) {
        load.invoke(context.loadCollections(parentId))
    }
}