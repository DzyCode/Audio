package com.audio.model.db

import android.database.sqlite.SQLiteDatabase
import com.audio.util.to
import org.jetbrains.anko.db.*
import org.jetbrains.anko.doAsync

class CollectionPlayManager {

    private val dbHelper by lazy { CollectionDatabase() }
    private var songCollectionIds: MutableList<Int>? = null
    private var albumCollectionIds: MutableList<Int>? = null

    companion object {
        val instance by lazy { CollectionPlayManager() }
    }

    private constructor() {
        doAsync {
            songCollectionIds = dbHelper.querySongCollection()
            albumCollectionIds = dbHelper.queryAlbumCollection()
        }
    }

    fun getCollectionCount(): Int {
        return dbHelper.dbSize()
    }

    fun getSongCollection(): MutableList<Int> {
        return songCollectionIds ?: dbHelper.querySongCollection()
    }

    fun getAlbumCollection(): MutableList<Int> {
        return albumCollectionIds ?: dbHelper.queryAlbumCollection()
    }

    fun insertSong(songId: Int) {
        songCollectionIds?.let {
            it.remove(songId)
            it.add(0, songId)
        }
        dbHelper.insert(songId, CollectionDatabase.SONG_COLLECTION)
    }

    fun insertAlbum(albumId: Int) {
        dbHelper.insert(albumId, CollectionDatabase.ALBUM_COLLECTION)
    }

    fun deleteSong(itemId: Int) {
        songCollectionIds?.remove(itemId)
        dbHelper.delete(itemId)
    }

    class CollectionDatabase : SqliteHelper {

        companion object {
            val versionCode = 1
            val dbName = "collections"
            val tableName = "collections"
            val ID = "_id"
            val ITEMID = "songid"
            val TYPE = "type"
            val TIME = "time"
            val SONG_COLLECTION = 0
            val ALBUM_COLLECTION = 1
        }

        constructor() : super(dbName, version = versionCode)

        override fun onCreate(p0: SQLiteDatabase?) {
            p0?.createTable(tableName, true,
                    ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                    ITEMID to INTEGER,
                    TYPE to INTEGER,
                    TIME to INTEGER)
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        }

        fun insert(songId: Int, type: Int) {
            use {
                insert(tableName,
                        ITEMID to songId,
                        TYPE to type,
                        TIME to System.currentTimeMillis())
            }
        }

        fun delete(id: Int) {
            use {
                delete(tableName, "$ITEMID = $id")
            }
        }

        fun dbSize(): Int {
            return use {
                select(tableName).exec { count }
            }
        }

        fun querySongCollection(): MutableList<Int> {
            return queryType(SONG_COLLECTION)
        }

        fun queryAlbumCollection(): MutableList<Int> {
            return queryType(ALBUM_COLLECTION)
        }

        private fun queryType(type: Int): MutableList<Int> {
            val result = mutableListOf<Int>()
            use {
                val list = select(tableName)
                        .whereArgs("$TYPE = $type")
                        .orderBy(TIME)
                        .parseList(object : RowParser<Int> {
                            override fun parseRow(columns: Array<Any?>): Int {
                                return columns[1]!!.to<Long>().toInt()
                            }
                        })
                result.addAll(list)
            }
            return result
        }
    }
}