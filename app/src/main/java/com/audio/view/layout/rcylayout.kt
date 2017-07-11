package com.audio.view.layout

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import audio.com.audio.R
import com.db.recycler.RcyItemDecoration
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.relativeLayout

class RcyLayout : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>): View {
        return with(ui, {
            relativeLayout {
                lparams(matchParent, matchParent)
                recyclerView {
                    id = R.id.rcyList
                    layoutManager = LinearLayoutManager(ctx)
                    addItemDecoration(RcyItemDecoration(ctx))
                }.lparams(matchParent, matchParent)
            }
        })
    }
}

