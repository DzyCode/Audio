package com.audio.view.layout

import android.graphics.Color
import android.view.ViewGroup
import android.widget.ImageView
import audio.com.audio.R
import org.jetbrains.anko.*

fun <T : ViewGroup> T.createPlayNavigationBar() {
    relativeLayout {
        id = R.id.audio_navigation_bar
        lparams(matchParent, wrapContent) {
            alignParentBottom()
        }
        backgroundColor = Color.WHITE
        imageView {
            id = R.id.audio_navigation_icon
            scaleType = ImageView.ScaleType.FIT_XY
            setImageResource(R.mipmap.ic_launcher)
        }.lparams(dip(56), dip(56)) {
            centerVertically()
            setMargins(dip(3), dip(3), dip(5), dip(3))
        }

        verticalLayout {
            textView {
                id = R.id.audio_navigation_title
                textSize = dip(5).toFloat()
            }.lparams(wrapContent, wrapContent) {
            }
            textView {
                id = R.id.audio_navigation_subtitle
                textSize = dip(3).toFloat()
            }.lparams(wrapContent, wrapContent) {
                setMargins(0, dip(4), 0, 0)
            }
        }.lparams(matchParent, wrapContent) {
            centerVertically()
            rightOf(R.id.audio_navigation_icon)
        }
        imageView {
            id = R.id.audio_navigation_playlist
            setImageResource(R.mipmap.playbar_btn_playlist)
        }.lparams(wrapContent, wrapContent) {
            centerVertically()
            alignParentRight()
        }
        imageView {
            id = R.id.audio_navigation_next
            setImageResource(R.mipmap.playbar_btn_next)
        }.lparams(wrapContent, wrapContent) {
            centerVertically()
            leftOf(R.id.audio_navigation_playlist)
        }
        imageView {
            id = R.id.audio_navigation_play
            setImageResource(R.mipmap.playbar_btn_play)
        }.lparams(wrapContent, wrapContent) {
            leftOf(R.id.audio_navigation_next)
            centerVertically()
            setMargins(0, 0, dip(5), 0)
        }

    }
}

