package com.audio.view.show

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import audio.com.audio.R
import com.audio.model.Song
import com.audio.model.SongHead
import com.audio.model.node.Node
import com.audio.play.SongQueueManager
import com.audio.present.DefaultPresent
import com.audio.util.*
import com.audio.util.agent.setSongQueue
import com.audio.view.layout.*
import com.audio.view.life.FrgLife
import com.db.recycler.RcyList
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk15.coroutines.onClick

class SongFrgShow : FrgLife() {
    lateinit var songPresent: DefaultPresent
    lateinit var songList: RecyclerView
    lateinit var songAdapt: RcyList
    var songHeadBind: (View?, Any) -> Unit = {
        view, any ->
        view?.let {
            val tag = it.tag as SongHeadView? ?: SongHeadView(view)
            tag.bindDate(any.to<SongHead>())
            it.tag = tag
            tag.head.onClick {
                addCurrentSongsToQueue("")
                playCurrentSong()
            }
        }
    }
    var songHeadView: (LayoutInflater, ViewGroup?) -> View = {
        layoutInflater, viewGroup ->
        viewGroup!!.context.createSongHeadView()
    }
    var songItemBind: (View?, Any) -> Unit = {
        view, any ->
        view?.let {
            val tag = it.tag as SongItemView? ?: SongItemView(view)
            tag.bindDate(any.to<Song>())
            it.tag = tag
            it.onClick {
                Node.SONGS.equals(false, songPresent.queueTitle()) {
                    title, currentTitle ->
                    addCurrentSongsToQueue(tag.song._id.toString())
                }
                songPresent.play(tag.song)
            }
        }
    }
    var songItemView: (LayoutInflater, ViewGroup?) -> View = {
        layoutInflater, viewGroup ->
        viewGroup!!.context.createSongItemView()
    }

    override fun receive(): (Fragment, LifeOrder, Any?) -> Any {
        return {
            fragment, lifeOrder, any ->
            when (lifeOrder) {
                LifeOrder.ONCREATEVIEW -> {
                    val view = initView(fragment)
                    initVariable(fragment)
                    view
                }
                LifeOrder.ONSTART -> onStart()
                LifeOrder.ONSTOP -> onStop()
                else -> initView(fragment)
            }
        }
    }

    private fun initView(fragment: Fragment): View {
        val parent = SongFrgLayout().createView(AnkoContext.create(fragment.context, fragment))
        songList = parent.find<RecyclerView>(R.id.rcyList)
        return parent
    }

    private fun initVariable(fragment: Fragment) {
        songPresent = DefaultPresent(fragment.activity!!)
        songAdapt = RcyList()
        songAdapt.registerType(SongHead::class.java, songHeadBind, songHeadView)
        songAdapt.registerType(Song::class.java, songItemBind, songItemView)
        songList.adapter = songAdapt.adapt()

    }

    private fun onStart() {
        songPresent.connect()
        songPresent.loadDataWithId<Any>(Node.SONGS, {
            s, list ->
            songAdapt.clear()
            songAdapt.addAll(list)
            songAdapt.notifyDataSetChanged()
            val songs = mutableListOf<Song>()
            list.forEach {
                if(it is Song) {
                    songs.add(it)
                }
            }
        })
    }

    private fun onStop() {
        songPresent.disconnect()
    }

    private fun addCurrentSongsToQueue(initialId : String) {
        setSongQueue(Node.SONGS,
                songAdapt.filter { it as? Song }, initialId)
    }

    private fun playCurrentSong() {
        SongQueueManager.instance.getCurrentSong()?.let {
            songPresent.play(it)
        }
    }
}

