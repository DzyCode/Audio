package com.audio.view.layout

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import audio.com.audio.R
import com.db.recycler.RcyItemDecoration
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.recyclerview.v7.recyclerView

class RecentlyPlayLayout : AnkoComponent<Activity> {
    override fun createView(ui: AnkoContext<Activity>): View {
        return with(ui, {
            relativeLayout {
                lparams(matchParent, matchParent)
                toolbar {
                    id = R.id.recently_play_bar
                    backgroundColor = ContextCompat.getColor(ctx, R.color.colorPrimary)
                    setTitle(R.string.recent_play)
                }.lparams(matchParent, wrapContent)
                recyclerView {
                    id = R.id.recently_play_list
                    layoutManager = LinearLayoutManager(ctx)
                    addItemDecoration(RcyItemDecoration(ctx))
                }.lparams(matchParent, matchParent) {
                    below(R.id.recently_play_bar)
                }
                createPlayNavigationBar()
            }
        })
    }
}

