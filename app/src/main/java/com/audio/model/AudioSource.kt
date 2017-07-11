package com.audio.model

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Files
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import audio.com.audio.R
import com.audio.model.db.RecentlyPlayManager
import com.audio.util.to
import com.audio.view.life.Show
import java.io.Serializable
import java.util.Comparator

fun <T : Context> T.closeCursor(cursor: Cursor?) {
    cursor?.close()
}

fun <T : Context> T.songsCursor(): Cursor? {
    val where = MediaStore.Audio.Media.IS_MUSIC + " = 1 AND " + MediaStore.Audio.Media.TITLE + " != ''"
    return contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            where,
            null,
            null)
}

fun <T : Context> T.songsNumber(): Int {
    return cursorCount(songsCursor())
}

fun <T : Context> T.recentPlayNumber(): Int {
    return RecentlyPlayManager.instance.getPlaylistCount()
}

fun <T : Context> T.cursorCount(cursor: Cursor?): Int {
    var number = cursor?.count ?: 0
    closeCursor(cursor)
    return number
}

fun <T : Serializable> T.convertToBroserableMediaItem(parentId: String): MediaBrowserCompat.MediaItem {
    return convertToMediaItem(parentId, "default", MediaBrowserCompat.MediaItem.FLAG_BROWSABLE)
}

fun <T : Serializable> T.convertToPlayableMediaItem(parentId: String, mediaId: String = "default"): MediaBrowserCompat.MediaItem {
    return convertToMediaItem(parentId, mediaId, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
}

fun <T : Serializable> T.convertToMediaItem(parentId: String, mediaId: String = "default", flag: Int): MediaBrowserCompat.MediaItem {
    return MediaBrowserCompat.MediaItem(convertToMediaDescriptionCompat(parentId, mediaId), flag)
}

fun <T : Serializable> T.convertToMediaDescriptionCompat(parentId: String, mediaId: String): MediaDescriptionCompat {
    var bundle = Bundle()
    bundle.putSerializable(parentId, this)
    return MediaDescriptionCompat.Builder().setExtras(bundle).setMediaId(mediaId).build()
}

fun <T : Context> T.loadAudioNavigationHeadItems(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    var result = mutableListOf<MediaBrowserCompat.MediaItem>()
    var list = mutableListOf<Int>(R.string.local_music, R.string.recent_play,
            R.string.my_collection)
    var icon = mutableListOf<Int>(R.mipmap.music_icn_local, R.mipmap.music_icn_recent,
            R.mipmap.music_icn_artist)
    var number = mutableListOf<Int>(songsNumber(), recentPlayNumber(), 0)
    var showTyoe = mutableListOf<Show>(Show.LOCALMUSICSHOW, Show.RECENTLYMUSICSHOW, Show.RECENTLYMUSICSHOW)
    for (index in 0 until list.size) {
        var item = AudioHeadItem(icon[index], getString(list[index]), number[index], showTyoe[index])
        result.add(item.convertToBroserableMediaItem(parentId))
    }
    return result
}

fun <T : Context> T.loadAudioNavigationSettingItems(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    var result = mutableListOf<MediaBrowserCompat.MediaItem>()
    var item = AudioSettingItem(R.mipmap.arrow, getString(R.string.music_list), 0, 0)
    result.add(item.convertToBroserableMediaItem(parentId))
    return result
}

fun <T : Context> T.loadSongHead(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    var songhead = SongHead(R.mipmap.list_icn_play, R.string.song_head_title, R.mipmap.list_icn_mng,
            R.string.song_head_subtitle, cursorCount(songsCursor()))
    return mutableListOf(songhead.convertToBroserableMediaItem(parentId))
}

fun <T : Context> T.loadLocalSongs(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    return load(parentId, songsCursor(), {
        cursor ->
        cursor.song()
    }, {
        song ->
        song._id.toString()
    })
}

fun Cursor.song(): Song {
    var id = getInt(getColumnIndex(MediaStore.Audio.Media._ID))
    var title = getString(getColumnIndex(MediaStore.Audio.Media.TITLE))
    var artist = getString(getColumnIndex(MediaStore.Audio.Media.ARTIST))
    var album = getString(getColumnIndex(MediaStore.Audio.Media.ALBUM))
    var track = getInt(getColumnIndex(MediaStore.Audio.Media.TRACK))
    var data = getString(getColumnIndex(MediaStore.Audio.Media.DATA))
    var druation = getLong(getColumnIndex(MediaStore.Audio.Media.DURATION))
    var size = getLong(getColumnIndex(MediaStore.Audio.Media.SIZE))
    var albumId = getLong(getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
    return Song(id, title, artist, album, track, data, druation, size, albumId)
}

fun <T : Context> T.singerCursor(): Cursor? {
    return contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, null, null, null, null)
}

fun Cursor.singer(): Singer {
    var id = getInt(getColumnIndex(MediaStore.Audio.Artists._ID))
    var title = getString(getColumnIndex(MediaStore.Audio.Artists.ARTIST))
    var numSongs = getInt(getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))
    return Singer(id, title, numSongs)
}

fun <T : Context> T.loadSingers(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    return load(parentId, singerCursor(), {
        cursor ->
        var singer = cursor.singer()
        var id = getAlbumIdFromSinger(singer._id)
        id?.let { singer.data = getAlbumArtUri(it).toString() }
        singer
    }, {
        artist ->
        artist._id.toString()
    })
}

fun <T : Context> T.albumCursor(): Cursor? {
    return contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null, null, null, null)
}

fun Cursor.album(): Album {
    var id = getInt(getColumnIndex(MediaStore.Audio.Albums._ID))
    var title = getString(getColumnIndex(MediaStore.Audio.Albums.ALBUM))
    var artist = getString(getColumnIndex(MediaStore.Audio.Albums.ARTIST))
    var artistId = getInt(getColumnIndex(MediaStore.Audio.Albums._ID))
    var numSongs = getInt(getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
    return Album(id, title, artist, artistId, numSongs)
}

fun <T : Context> T.loadAlbums(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    return load(parentId, albumCursor(), {
        cursor ->
        cursor.album()
    }, {
        album ->
        album._id.toString()
    })
}

fun <T : Context, E : Serializable> T.load(parentId: String, cursor: Cursor?,
                                           block: (Cursor) -> E, id: (E) -> String): MutableList<MediaBrowserCompat.MediaItem> {
    var result = mutableListOf<MediaBrowserCompat.MediaItem>()
    try {
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val data = block.invoke(it)
                    result.add(data.convertToPlayableMediaItem(parentId, id.invoke(data)))
                } while (cursor.moveToNext())
            }
        }
    } catch (e: Exception) {
    } finally {
        closeCursor(cursor)
    }
    return result
}

fun <T : Context> T.folderCursor(): Cursor? {
    val uri = MediaStore.Files.getContentUri("external")
    var where = Files.FileColumns.MEDIA_TYPE + " = " + Files.FileColumns.MEDIA_TYPE_AUDIO
    return contentResolver.query(uri, null, where, null, null)
}

fun Any.getAlbumArtUri(albumId: Long): Uri {
    return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId)
}

fun Song.picUri(): Uri {
    return getAlbumArtUri(albumId)
}

fun Album.picUri(): Uri {
    return getAlbumArtUri(_id.toLong())
}

fun <T : Context> T.getAlbumIdFromSinger(singerId: Int): Long? {
    val where = MediaStore.Audio.Media.IS_MUSIC + " = 1 AND " + MediaStore.Audio.Media.TITLE + " != '' AND " +
            MediaStore.Audio.Media.ARTIST_ID + " = $singerId"
    val projection = arrayOf(MediaStore.Audio.Media.ALBUM_ID)
    contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            where,
            null,
            null)?.let {
        try {
            if (it.moveToFirst()) {
                return it.getInt(0).toLong()
            }
        } catch (e: Exception) {
        } finally {
            closeCursor(it)
        }
    }

    return null
}

fun <T : Context> T.getRecentlyPlayList(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    var map = RecentlyPlayManager.instance.getPlayList()
    var set = map.keys
    var result = load(parentId, recentPlayCursor(set), {
        cursor ->
        cursor.song()
    }, {
        song ->
        song._id.toString()
    })
    result.sortWith(object : Comparator<MediaBrowserCompat.MediaItem> {
        override fun compare(p0: MediaBrowserCompat.MediaItem?, p1: MediaBrowserCompat.MediaItem?): Int {
            var before = p0?.description?.extras?.getSerializable(parentId)?.to<Song>()?._id ?: 0
            var after = p1?.description?.extras?.getSerializable(parentId)?.to<Song>()?._id ?: 0
            var beforeTime = map.get(before)?.time ?: 0
            var afterTime = map.get(after)?.time ?: 0
            return (afterTime - beforeTime).toInt()
        }
    })
    return result
}

fun <T : Context> T.recentPlayCursor(set: Set<Int>): Cursor? {
    if (set.size == 0) return null
    var inety = "("
    set.forEach {
        int ->
        inety = if (int != set.last()) "$inety $int ," else "$inety $int )"
    }
    val where = "${MediaStore.Audio.Media.IS_MUSIC} = 1 AND ${MediaStore.Audio.Media.TITLE}!= ''" +
            " AND ${MediaStore.Audio.Media._ID} in $inety"
    return contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            where,
            null,
            null)
}

fun <T : Context> T.loadRecentlyPlayHeader(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    var songhead = SongHead(R.mipmap.list_icn_play, R.string.song_head_title, R.mipmap.list_icn_mng,
            R.string.song_head_subtitle, RecentlyPlayManager.instance.getPlaylistCount())
    return mutableListOf(songhead.convertToBroserableMediaItem(parentId))
}