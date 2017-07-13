package com.audio.model.node

import android.content.Context
import android.support.v4.media.MediaBrowserCompat

abstract class Node {
    companion object {
        val NAVIGATION = "navigation"
        val SONGS = "songs"
        val SINGER = "singer"
        val ALBUMS = "albums"
        val FOLDER = "folder"
        val RECENTPLAY = "recentplay"
        val SONGCOLLECTION = "songcollection"
        fun node(nodeId: String) : Node{
            when(nodeId) {
                NAVIGATION -> return NavigationNode()
                SONGS -> return SongsNode()
                SINGER -> return SingerNode()
                ALBUMS -> return AlbumNode()
                RECENTPLAY -> return RecentlyPlayNode()
                SONGCOLLECTION -> return SongCollectionNode()
                else -> return EMPTY()
            }
        }
    }
    abstract fun load(context: Context, parentId : String,
                      load : (MutableList<MediaBrowserCompat.MediaItem>) -> Unit)

    class EMPTY : Node() {
        override fun load(context: Context, parentId: String, load: (MutableList<MediaBrowserCompat.MediaItem>) -> Unit) {

        }
    }
}


