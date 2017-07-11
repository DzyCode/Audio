package com.audio.model

import com.audio.view.life.Show
import java.io.Serializable

data class AudioHeadItem(var icon: Int, var title: String, var numSongs: Int = 0, var showType : Show) : Serializable

data class AudioSettingItem(var icon: Int, var title: String, var numItem: Int, var settingIcom: Int) : Serializable

data class SongHead(var icon: Int, var title: Int, var settingIcon: Int,
                    var settingTitle: Int, var songCount: Int) : Serializable

data class Song(var _id: Int, var title: String, var artist: String, var album: String,
                var track: Int, var data: String, var duration: Long, var size: Long = 0,
                var albumId: Long) : Serializable

data class Album(var _id: Int, var title: String, var artist: String, var artistId: Int,
                 var numSongs: Int) : Serializable

data class Singer(var _id: Int, var title: String, var numSongs: Int, var data: String? = null) : Serializable

data class RecentlyPlayList(var _id: Int, var time: Long)
