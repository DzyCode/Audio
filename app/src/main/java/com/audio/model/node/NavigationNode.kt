package com.audio.model.node

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.audio.model.loadAudioNavigationHeadItems
import com.audio.model.loadAudioNavigationSettingItems

class NavigationNode : Node() {
    override fun load(context: Context, parentId: String,
                      load: (MutableList<MediaBrowserCompat.MediaItem>) -> Unit) {
        var list = mutableListOf<MediaBrowserCompat.MediaItem>()
        list.addAll(context.loadAudioNavigationHeadItems(parentId))
        list.addAll(context.loadAudioNavigationSettingItems(parentId))
        load.invoke(list)
    }
}

