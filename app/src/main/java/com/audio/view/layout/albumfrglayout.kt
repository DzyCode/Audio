package com.audio.view.layout

import android.support.v4.app.Fragment
import android.view.View
import org.jetbrains.anko.*

class AlbumFrgLayout : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>): View {
        return with(ui, {
            RcyLayout().createView(ui)
        })
    }
}

