package com.audio.model.db

import android.database.sqlite.SQLiteDatabase
import com.audio.model.RecentlyPlayList
import com.audio.util.to
import org.jetbrains.anko.db.*
import org.jetbrains.anko.doAsync

class RecentlyPlayManager {
    private var recentPlaylist: MutableMap<Int, RecentlyPlayList>? = null
    private val dbHelper by lazy { RecentlyDatabase() }

    companion object {
        val instance: RecentlyPlayManager by lazy { RecentlyPlayManager() }
    }

    private constructor() {
        doAsync {
            if(recentPlaylist == null) {
                recentPlaylist = dbHelper.queryAll()
            }
        }
    }

    fun getPlaylistCount(): Int {
        return recentPlaylist?.size ?: dbHelper.dbSize()
    }

    fun getPlayList() : MutableMap<Int, RecentlyPlayList> {
        return recentPlaylist ?: dbHelper.queryAll()
    }

    fun insertOrUpdate(playData: RecentlyPlayList) {
        doAsync {
            recentPlaylist?.let {
                it.get(playData._id)?.let { dbHelper.update(playData) } ?: dbHelper.insert(playData)
                it.put(playData._id, playData)
            }
        }
    }

    fun insertOrUpdate(playId: Int) {
        insertOrUpdate(RecentlyPlayList(playId, System.currentTimeMillis()))
    }

    class RecentlyDatabase : SqliteHelper {
        companion object {
            val versionCode: Int = 1
            val ID = "_Id"
            val ADD_TIME = "add_time"
            val tableName = "recentplaylist"
        }

        constructor() : super(version = versionCode)

        override fun onCreate(p0: SQLiteDatabase?) {
            p0?.let {
                it.createTable(tableName, true,
                        ID to INTEGER + PRIMARY_KEY,
                        ADD_TIME to INTEGER)
            }

        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        }

        fun insert(playData: RecentlyPlayList) {
            use {
                insert(tableName,
                        ID to playData._id,
                        ADD_TIME to playData.time)
            }
        }

        fun update(playData: RecentlyPlayList) {
            use {
                update(tableName,
                        ID to playData._id,
                        ADD_TIME to playData.time)
            }
        }

        fun dbSize(): Int {
            return use {
                select(tableName).exec { count }
            }
        }

        fun queryAll(): MutableMap<Int, RecentlyPlayList> {
            val result = mutableMapOf<Int, RecentlyPlayList>()
            use {
                select(tableName)
                        .orderBy(ADD_TIME)
                        .parseList(object : RowParser<RecentlyPlayList> {
                            override fun parseRow(columns: Array<Any?>): RecentlyPlayList {
                                val playlist = RecentlyPlayList(columns[0]!!.to<Int>(), columns[1]!!.to<Long>())
                                result.put(playlist._id, playlist)
                                return playlist
                            }
                        })
            }
            return result
        }

        inner class Parse : RowParser<RecentlyPlayList> {
            override fun parseRow(columns: Array<Any?>): RecentlyPlayList {
                return RecentlyPlayList(columns[0]!!.to<Int>(), columns[1]!!.to<Long>())
            }
        }
    }
}

