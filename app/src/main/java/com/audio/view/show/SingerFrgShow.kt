package com.audio.view.show

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import audio.com.audio.R
import com.audio.model.Singer
import com.audio.model.node.Node
import com.audio.present.DefaultPresent
import com.audio.util.LifeOrder
import com.audio.util.to
import com.audio.view.layout.SingerFrgLayout
import com.audio.view.layout.SingerItemView
import com.audio.view.layout.createLocalItem
import com.audio.view.life.FrgLife
import com.db.recycler.RcyList
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find

class SingerFrgShow : FrgLife() {

    lateinit var present : DefaultPresent
    lateinit var rcyList : RecyclerView
    lateinit var adapt : RcyList
    var singerBind: (View?, Any) -> Unit = {
        view, any ->
        view?.let {
            val tag = it.tag as SingerItemView? ?: SingerItemView(it)
            tag.bind(any.to<Singer>())
            it.tag = tag
        }
    }
    var singerItemView: (LayoutInflater, ViewGroup?) -> View = {
        layoutInflater, viewGroup ->
        viewGroup!!.context.createLocalItem()
    }

    override fun receive(): (Fragment, LifeOrder, Any?) -> Any {
        return {
            fragment, lifeOrder, any ->
            when (lifeOrder) {
                LifeOrder.ONCREATEVIEW -> {
                    initVariable(fragment, initView(fragment))
                }
                LifeOrder.ONSTART -> onStart()
                LifeOrder.ONSTOP -> onStop()
                else -> Any()
            }
        }
    }

    private fun initView(fragment: Fragment): View {
        return SingerFrgLayout().createView(AnkoContext.create(fragment.context, fragment))
    }

    private fun initVariable(fragment: Fragment, view: View): View {
        rcyList = view.find<RecyclerView>(R.id.rcyList)
        present = DefaultPresent(fragment.activity)
        adapt = RcyList()
        adapt.registerType(Singer::class.java, singerBind, singerItemView)
        rcyList.adapter = adapt.adapt()
        return view
    }

    private fun onStart() {
        present.connect()
        present.loadDataWithId<Any>(Node.SINGER, {
            s, list ->
            adapt.clear()
            adapt.addAll(list)
            adapt.notifyDataSetChanged()
        })
    }

    private fun onStop() {
        present.disconnect()
    }
}

