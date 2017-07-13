package com.audio.view.layout

import android.support.v4.app.Fragment
import android.view.View
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext


class SongCollectionFrgLayout : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>): View {
        return with(ui, {
            RcyLayout().createView(ui)
        })
    }
}