package com.db.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup


class RcyAdapt() : RecyclerView.Adapter<Holder>() {

    lateinit var rcyList : RcyList

    constructor(rcyList : RcyList) : this(){
        this.rcyList = rcyList
    }
    override fun getItemCount(): Int {
        return rcyList.size
    }

    override fun getItemViewType(position: Int): Int {
        return rcyList.itemType(position)
    }
    override fun onBindViewHolder(holder: Holder?, position: Int) {
        rcyList.bindViewHold(holder?.itemView, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        return Holder(rcyList.createItemView(parent, viewType))
    }
}

class Holder(itemview : View?) : RecyclerView.ViewHolder(itemview) {

}