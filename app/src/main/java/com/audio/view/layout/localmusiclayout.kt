package com.audio.view.layout

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.view.View
import audio.com.audio.R
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager

class LocalMusicLayout : AnkoComponent<Activity> {
    override fun createView(ui: AnkoContext<Activity>): View {
        return with(ui, {
            relativeLayout {
                lparams(matchParent, matchParent)

                toolbar {
                    id = R.id.local_music_bar
                    backgroundColor = ContextCompat.getColor(ctx, R.color.colorPrimary)
                }.lparams(matchParent, wrapContent)

                tabLayout {
                    id = R.id.local_music_tablayout
                }.lparams(matchParent, wrapContent) {
                    below(R.id.local_music_bar)
                }

                viewPager {
                    id = R.id.local_music_viewpage
                }.lparams(matchParent, matchParent) {
                    below(R.id.local_music_tablayout)
                    above(R.id.audio_navigation_bar)
                }
                createPlayNavigationBar()
            }
        })
    }
}

