package com.audio.view.layout

import android.support.v4.app.Fragment
import android.view.View
import audio.com.audio.R
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class SongFrgLayout : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>): View {
        return with(ui, {
            RcyLayout().createView(ui)
//            relativeLayout {
//                lparams(matchParent, matchParent)
//                recyclerView {
//                    id = R.id.song_frg_list
//                }.lparams(matchParent, matchParent)
//            }
        })
    }
}
