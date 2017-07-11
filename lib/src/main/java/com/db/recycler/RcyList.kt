package com.db.recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.concurrent.atomic.AtomicInteger


class RcyList : ArrayList<Any>() {

    private var autoType = AtomicInteger()
    private var mapOfClazz = mutableMapOf<String, Item>()
    private var mapOfType = mutableMapOf<Int, Item>()
    private val rcyAdapt: RcyAdapt by lazy { RcyAdapt(this@RcyList) }

    fun bindViewHold(view: View?, pos: Int) {
        mapOfClazz.get(get(pos).javaClass.name)?.bindData?.invoke(view, get(pos))
    }

    fun createItemView(parent: ViewGroup?, viewType: Int): View {
        return mapOfType.get(viewType)?.genView?.invoke(LayoutInflater.from(parent?.context), parent)!!
    }

    fun itemType(position: Int): Int {
        return mapOfClazz.get(get(position).javaClass.name)?.type ?: 0
    }

    fun registerType(clazz: Class<*>, bindData: ((View?, Any) -> Unit),
                     genView: ((LayoutInflater, ViewGroup?) -> View)) {
        mapOfClazz.put(clazz.name, Item(clazz = clazz.name, type = autoType.incrementAndGet(), genView = genView, bindData = bindData))
        mapOfType.put(autoType.get(), Item(clazz = clazz.name, type = autoType.get(), genView = genView, bindData = bindData))
    }

    fun adapt(): RecyclerView.Adapter<*> {
        return rcyAdapt
    }

    fun notifyDataSetChanged() {
        rcyAdapt.notifyDataSetChanged()
    }

    fun notifyItemChanged(position: Int) {
        rcyAdapt.notifyItemRangeChanged(position, 1)
    }

    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
        rcyAdapt.notifyItemRangeChanged(positionStart, itemCount);
    }

    fun notifyItemInserted(position: Int) {
        rcyAdapt.notifyItemRangeInserted(position, 1)
    }

    fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
        rcyAdapt.notifyItemMoved(fromPosition, toPosition)
    }

    fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        rcyAdapt.notifyItemRangeInserted(positionStart, itemCount)
    }

    fun notifyItemRemoved(position: Int) {
        rcyAdapt.notifyItemRangeRemoved(position, 1);
    }

    fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        rcyAdapt.notifyItemRangeRemoved(positionStart, itemCount)
    }
}

data class Item(var clazz: String, var type: Int, var genView: ((LayoutInflater, ViewGroup?) -> View?),
                var bindData: ((View?, Any) -> Unit))