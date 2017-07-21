package com.audio.view.show

import android.app.Activity
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import audio.com.audio.R
import com.audio.model.Song
import com.audio.model.SongHead
import com.audio.model.node.Node
import com.audio.play.SongQueueManager
import com.audio.present.RecentlyPlayPresent
import com.audio.present.base.ICallBack
import com.audio.util.agent.setSongQueue
import com.audio.util.filter
import com.audio.util.to
import com.audio.view.layout.*
import com.audio.view.life.AtyLife
import com.audio.view.widget.PlayBar
import com.db.recycler.RcyList
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.jetbrains.anko.setContentView
import kotlin.properties.Delegates

class RecentlyPlayShow : AtyLife, ICallBack {

    private lateinit var parent: View
    private lateinit var context: Activity
    private val toolbar by lazy { parent.find<Toolbar>(R.id.recently_play_bar) }
    private val recentList by lazy { parent.find<RecyclerView>(R.id.recently_play_list) }
    private var playPresent by Delegates.notNull<RecentlyPlayPresent>()
    val playBar: PlayBar by lazy { PlayBar(parent, context) }
    private var adapt = RcyList()
    var songHeadBind: (View?, Any) -> Unit = {
        view, any ->
        view?.let {
            val tag = it.tag as SongHeadView? ?: SongHeadView(view)
            tag.bindDate(any.to<SongHead>())
            it.tag = tag
            it.onClick {
                addCurrentSongsToQueue()
                playCurrentSong()
            }
        }
    }
    var songHeadView: (LayoutInflater, ViewGroup?) -> View = {
        layoutInflater, viewGroup ->
        viewGroup!!.context.createSongHeadView()
    }
    var recentlyItemBind: (View?, Any) -> Unit = {
        view, any ->
        view?.let {
            val tag = it.tag as RecentlyPlayItemView? ?: RecentlyPlayItemView(view)
            tag.bindDate(any.to<Song>())
            it.tag = tag
            it.onClick {
                addCurrentSongsToQueue(tag.song._id.toString())
                playCurrentSong()
            }
        }
    }
    var recentlyItemView: (LayoutInflater, ViewGroup?) -> View = {
        layoutInflater, viewGroup ->
        viewGroup!!.context.createRecentlyPlayItemView()
    }

    override fun onCreate(context: Activity, any: Any?) {
        this.context = context
        parent = RecentlyPlayLayout().setContentView(context)
        context.to<AppCompatActivity>().let {
            it.setSupportActionBar(toolbar)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        adapt.registerType(SongHead::class.java, songHeadBind, songHeadView)
        adapt.registerType(Song::class.java, recentlyItemBind, recentlyItemView)
        recentList.adapter = adapt.adapt()
        playPresent = RecentlyPlayPresent(context)
        playPresent.registerCallback(this)
        toolbar.setNavigationOnClickListener {
            context.onBackPressed()
        }
    }

    override fun onStart() {
        playPresent.connect()
        playBar.play(SongQueueManager.instance.getCurrentSong())
        playPresent.loadDataWithId<Any>(Node.RECENTPLAY, {
            parentId, data ->
            adapt.clear()
            adapt.addAll(data)
            adapt.notifyDataSetChanged()
        })
    }

    override fun onStop() {
        playPresent.disconnect()
    }

    private fun addCurrentSongsToQueue(initialId: String = "") {
        setSongQueue(Node.RECENTPLAY,
                adapt.filter { it as? Song }, initialId)
    }

    private fun playCurrentSong() {
        SongQueueManager.instance.getCurrentSong()?.let {
            playPresent.play(it)
        }
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
        state?.let {
            when (it.state) {
                PlaybackStateCompat.STATE_PLAYING -> {
                    playBar.play(SongQueueManager.instance.getCurrentSong())
                }
                PlaybackStateCompat.STATE_PAUSED -> playBar.pause()
            }
        }
    }
}
