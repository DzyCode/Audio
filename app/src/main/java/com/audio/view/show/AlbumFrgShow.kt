package com.audio.view.show

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import audio.com.audio.R
import com.audio.model.Album
import com.audio.model.node.Node
import com.audio.present.AlbumPresent
import com.audio.util.to
import com.audio.view.layout.AlbumFrgLayout
import com.audio.view.layout.AlbumItemView
import com.audio.view.layout.createLocalItem
import com.audio.view.life.FrgLife
import com.db.recycler.RcyList
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find
import kotlin.properties.Delegates

class AlbumFrgShow : FrgLife {

    var present by Delegates.notNull<AlbumPresent>()
    var rcyList by Delegates.notNull<RecyclerView>()
    var adapt by Delegates.notNull<RcyList>()
    var albumBind: (View?, Any) -> Unit = {
        view, any ->
        view?.let {
            val tag = it.tag as AlbumItemView? ?: AlbumItemView(it)
            tag.bind(any.to<Album>())
            it.tag = tag
        }
    }
    var albumItemView: (LayoutInflater, ViewGroup?) -> View = {
        layoutInflater, viewGroup ->
        viewGroup!!.context.createLocalItem()
    }

    override fun onCreateView(context: Fragment, any: Any?): Any {
        return initVariable(context,
                AlbumFrgLayout().createView(AnkoContext.create(context.context, context)))
    }

    private fun initVariable(fragment: Fragment, view: View): View {
        present = AlbumPresent(fragment.activity)
        rcyList = view.find<RecyclerView>(R.id.rcyList)
        adapt = RcyList()
        adapt.registerType(Album::class.java, albumBind, albumItemView)
        rcyList.adapter = adapt.adapt()
        return view
    }

    override fun onStart() {
        present.connect()
        present.loadDataWithId<Any>(Node.ALBUMS, {
            s, list ->
            adapt.clear()
            adapt.addAll(list)
            adapt.notifyDataSetChanged()
        })
    }

    override fun onStop() {
        present.disconnect()
    }
}

