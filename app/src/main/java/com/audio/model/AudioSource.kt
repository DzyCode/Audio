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
import com.audio.model.db.CollectionPlayManager
import com.audio.model.db.RecentlyPlayManager
import com.audio.util.to
import com.audio.view.life.Show
import java.io.Serializable
import java.util.Comparator

fun <T : Any> T.closeCursor(cursor: Cursor?) {
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

fun <T : Any> T.recentPlayNumber(): Int {
    return RecentlyPlayManager.instance.getPlaylistCount()
}

fun <T : Any> T.collectionPlayNumber(): Int {
    return CollectionPlayManager.instance.getCollectionCount()
}

fun <T : Any> T.cursorCount(cursor: Cursor?): Int {
    val number = cursor?.count ?: 0
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
    val bundle = Bundle()
    bundle.putSerializable(parentId, this)
    return MediaDescriptionCompat.Builder().setExtras(bundle).setMediaId(mediaId).build()
}

fun <T : Context> T.loadAudioNavigationHeadItems(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    val result = mutableListOf<MediaBrowserCompat.MediaItem>()
    val list = mutableListOf(R.string.local_music, R.string.recent_play,
            R.string.my_collection)
    val icon = mutableListOf(R.mipmap.music_icn_local, R.mipmap.music_icn_recent,
            R.mipmap.music_icn_artist)
    val number = mutableListOf(songsNumber(), recentPlayNumber(), collectionPlayNumber())
    val showTyoe = mutableListOf(Show.LOCALMUSICSHOW, Show.RECENTLYMUSICSHOW, Show.COLLECTIONSHOW)
    (0 until list.size)
            .map { AudioHeadItem(icon[it], getString(list[it]), number[it], showTyoe[it]) }
            .mapTo(result) { it.convertToBroserableMediaItem(parentId) }
    return result
}

fun <T : Context> T.loadAudioNavigationSettingItems(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    val result = mutableListOf<MediaBrowserCompat.MediaItem>()
    val item = AudioSettingItem(R.mipmap.arrow, getString(R.string.music_list), 0, 0)
    result.add(item.convertToBroserableMediaItem(parentId))
    return result
}

fun <T : Context> T.loadSongHead(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    val songhead = SongHead(R.mipmap.list_icn_play, R.string.song_head_title, R.mipmap.list_icn_mng,
            R.string.song_head_subtitle, cursorCount(songsCursor()))
    return mutableListOf(songhead.convertToBroserableMediaItem(parentId))
}

fun <T : Context> T.loadLocalSongs(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    return load(parentId, songsCursor(), {
        cursor ->
        cursor.song()
    }, {
        it._id.toString()
    })
}

fun Cursor.song(): Song {
    val id = getInt(getColumnIndex(MediaStore.Audio.Media._ID))
    val title = getString(getColumnIndex(MediaStore.Audio.Media.TITLE))
    val artist = getString(getColumnIndex(MediaStore.Audio.Media.ARTIST))
    val album = getString(getColumnIndex(MediaStore.Audio.Media.ALBUM))
    val track = getInt(getColumnIndex(MediaStore.Audio.Media.TRACK))
    val data = getString(getColumnIndex(MediaStore.Audio.Media.DATA))
    val druation = getLong(getColumnIndex(MediaStore.Audio.Media.DURATION))
    val size = getLong(getColumnIndex(MediaStore.Audio.Media.SIZE))
    val albumId = getLong(getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
    return Song(id, title, artist, album, track, data, druation, size, albumId)
}

fun <T : Context> T.singerCursor(): Cursor? {
    return contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, null, null, null, null)
}

fun Cursor.singer(): Singer {
    val id = getInt(getColumnIndex(MediaStore.Audio.Artists._ID))
    val title = getString(getColumnIndex(MediaStore.Audio.Artists.ARTIST))
    val numSongs = getInt(getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))
    return Singer(id, title, numSongs)
}

fun <T : Context> T.loadSingers(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    return load(parentId, singerCursor(), {
        cursor ->
        val singer = cursor.singer()
        val id = getAlbumIdFromSinger(singer._id)
        id?.let { singer.data = getAlbumArtUri(it).toString() }
        singer
    }, {
        it._id.toString()
    })
}

fun <T : Context> T.albumCursor(): Cursor? {
    return contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null, null, null, null)
}

fun Cursor.album(): Album {
    val id = getInt(getColumnIndex(MediaStore.Audio.Albums._ID))
    val title = getString(getColumnIndex(MediaStore.Audio.Albums.ALBUM))
    val artist = getString(getColumnIndex(MediaStore.Audio.Albums.ARTIST))
    val artistId = getInt(getColumnIndex(MediaStore.Audio.Albums._ID))
    val numSongs = getInt(getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
    return Album(id, title, artist, artistId, numSongs)
}

fun <T : Context> T.loadAlbums(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
    return load(parentId, albumCursor(), {
        it.album()
    }, {
        it._id.toString()
    })
}

fun <T : Context, E : Serializable> T.load(parentId: String, cursor: Cursor?,
                                           block: (Cursor) -> E, id: (E) -> String): MutableList<MediaBrowserCompat.MediaItem> {
    val result = mutableListOf<MediaBrowserCompat.MediaItem>()
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
    val where = Files.FileColumns.MEDIA_TYPE + " = " + Files.FileColumns.MEDIA_TYPE_AUDIO
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
    val map = RecentlyPlayManager.instance.getPlayList()
    val set = map.keys
    val result = load(parentId, songInIdSetCursor(set), {
        it.song()
    }, {
        it._id.toString()
    })
    result.sortWith(Comparator<MediaBrowserCompat.MediaItem> { p0, p1 ->
        val before = p0?.description?.extras?.getSerializable(parentId)?.to<Song>()?._id ?: 0
        val after = p1?.description?.extras?.getSerializable(parentId)?.to<Song>()?._id ?: 0
        val beforeTime = map[before]?.time ?: 0
        val afterTime = map[after]?.time ?: 0
        (afterTime - beforeTime).toInt()
    })
    return result
}

fun <T : Context> T.songInIdSetCursor(set: Set<Int>): Cursor? {
    if (set.isEmpty()) return null
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
    val songhead = SongHead(R.mipmap.list_icn_play, R.string.song_head_title, R.mipmap.list_icn_mng,
            R.string.song_head_subtitle, RecentlyPlayManager.instance.getPlaylistCount())
    return mutableListOf(songhead.convertToBroserableMediaItem(parentId))
}

fun <T : Context> T.loadCollections(parentId: String) : MutableList<MediaBrowserCompat.MediaItem> {
    val ids = CollectionPlayManager.instance.getSongCollection()
    val result = load(parentId, songInIdSetCursor(ids.toSet()), {
        it.song() },{
        it._id.toString()
    })

    return result
}