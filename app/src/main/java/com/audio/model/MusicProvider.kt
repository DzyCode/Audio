package com.audio.model

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.audio.model.node.Node

class MusicProvider {
    val context : Context
    val nodeMap : MutableMap<String, Node>
    constructor(context: Context) {
        this.context = context
        nodeMap = mutableMapOf()
    }
    fun registNode(parentId : String) {
        val value = nodeMap[parentId]
        if (value == null) {
            nodeMap.put(parentId, Node.node(parentId))
        }
    }

    fun load(parentId: String, load : (MutableList<MediaBrowserCompat.MediaItem>) -> Unit) {
        nodeMap[parentId]?.load(context, parentId, load)
    }
}

