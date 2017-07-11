package com.audio.view.layout

import android.app.Activity
import android.graphics.Color
import android.view.View
import audio.com.audio.R
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar

class DetailAtyLayout : AnkoComponent<Activity> {
    override fun createView(ui: AnkoContext<Activity>): View {
        return with(ui, {
            relativeLayout {
                lparams(matchParent, matchParent)
                setBackgroundColor(Color.GRAY)
                toolbar {
                    id = R.id.play_head
                }.lparams(matchParent, wrapContent) {
                    alignParentTop()
                }
                detailplayshowview {
                    id = R.id.play_show
                }.lparams(matchParent, wrapContent) {
                    below(R.id.play_head)
                }
                detailplayinfoview {
                    id = R.id.play_info
                }.lparams(matchParent, wrapContent) {
                    above(R.id.play_progress_view)
                }
                detailplayprogressview {
                    id = R.id.play_progress_view
                }.lparams(matchParent, wrapContent) {
                    above(R.id.play_controll_view)
                }
                detailplaycontrolview {
                    id = R.id.play_controll_view
                }.lparams(matchParent, wrapContent) {
                    alignParentBottom()
                }
            }
        })
    }
}