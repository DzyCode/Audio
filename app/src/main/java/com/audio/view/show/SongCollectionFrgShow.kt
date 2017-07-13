package com.audio.view.show

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import audio.com.audio.R
import com.audio.model.Song
import com.audio.model.node.Node
import com.audio.present.DefaultPresent
import com.audio.present.base.ICallBack
import com.audio.util.*
import com.audio.util.agent.currentSong
import com.audio.util.agent.logd
import com.audio.util.agent.setSongQueue
import com.audio.view.layout.*
import com.audio.view.life.FrgLife
import com.db.recycler.RcyList
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk15.coroutines.onClick

class SongCollectionFrgShow : FrgLife(), ICallBack {
    lateinit var present: DefaultPresent
    lateinit var rcyList: RecyclerView
    lateinit var adapt: RcyList
    var songItemBind: (View?, Any) -> Unit = {
        view, any ->
        view?.let {
            val tag = it.tag as SongItemView? ?: SongItemView(view)
            tag.bindDate(any.to<Song>())
            it.tag = tag
            it.onClick {
                isSelfQueue().no { songQueueChanged(adapt, any.to<Song>()._id.toString()) }
                present.play(any.to())
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
                LifeOrder.ONCREATEVIEW -> initVariable(fragment, onCreateView(fragment))
                LifeOrder.ONSTART -> onStart()
                LifeOrder.ONRESUME -> onResume()
                LifeOrder.ONDESTROY -> onDestroy()
                else -> Any()
            }
        }
    }

    private fun onCreateView(fragment: Fragment): View {
        return SongCollectionFrgLayout().createView(AnkoContext.create(fragment.context, fragment))
    }

    private fun initVariable(fragment: Fragment, view: View): View {
        rcyList = view.find<RecyclerView>(R.id.rcyList)
        present = DefaultPresent(fragment.activity)
        present.connect()
        present.registerCallback(this)
        adapt = RcyList()
        adapt.registerType(Song::class.java, songItemBind, songItemView)
        rcyList.adapter = adapt.adapt()
        return view
    }

    private fun onStart() {
        logd(this, "start load data")
        present.loadDataWithId<Any>(Node.SONGCOLLECTION, {
            s, list ->
            logd(this, "load data success: ${System.currentTimeMillis()}  $list")
            updateListDatas(list)
            songQueueChanged(list)
        })
    }

    private fun onResume() {
    }

    private fun onDestroy() {
        present.disconnect()
    }

    private fun songQueueChanged(list: List<Any>, initId: String? = currentSong()?._id.toString()) {
        setSongQueue(Node.SONGCOLLECTION, list.filter { it as? Song }, initId)
    }

    private fun updateListDatas(list: List<Any>) {
        adapt.clear()
        adapt.addAll(list)
        adapt.notifyDataSetChanged()
    }

    private fun isSelfQueue(): Boolean {
        return Node.SONGCOLLECTION == present.queueTitle()
    }
}