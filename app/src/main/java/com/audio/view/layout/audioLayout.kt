package com.audio.view.layout

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import audio.com.audio.R
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.navigationView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4._DrawerLayout
import org.jetbrains.anko.support.v4.drawerLayout


class AudioLayout : AnkoComponent<Activity> {
    override fun createView(ui: AnkoContext<Activity>): View {
        return with(ui, {
            drawerLayout {
                id = R.id.drawlayout
                lparams {
                    width = matchParent
                    height = matchParent
                    fitsSystemWindows = true
                }
                createAppbarLayout(ui)
                navigationView{
                    id = R.id.navigation
                    inflateHeaderView(R.layout.head)
                    inflateMenu(R.menu.audio_menu)
                }.lparams {
                    width = wrapContent
                    height = matchParent
                    gravity = Gravity.START
                }
            }
        })
    }

    fun _DrawerLayout.createAppbarLayout(ui : AnkoContext<Activity>) {
        relativeLayout {
            appBarLayout {
                id = R.id.appbar
                toolbar {
                    id = R.id.toobar
                }.lparams(matchParent, wrapContent)
            }.lparams(matchParent, wrapContent)

            recyclerView {
                id = R.id.audio_list
                layoutManager = LinearLayoutManager(context)
            }.lparams(matchParent, matchParent) {
                below(R.id.appbar)
            }
            createPlayNavigationBar()
        }.lparams(matchParent, wrapContent)
    }
}
