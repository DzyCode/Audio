package com.audio.view.layout

import android.support.v4.app.Fragment
import android.view.View
import org.jetbrains.anko.*

class FolderFrgLayout : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>): View {
        return with(ui, {
            relativeLayout {
                lparams(matchParent, matchParent)
                button {
                    text = "folder"
                }.lparams(matchParent, wrapContent)
            }
        })
    }
}

