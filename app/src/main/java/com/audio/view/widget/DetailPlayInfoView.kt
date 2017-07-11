package com.audio.view.widget

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import audio.com.audio.R
import org.jetbrains.anko.*
import kotlin.properties.Delegates

class DetailPlayInfoView : _RelativeLayout {
    private var playInfo by Delegates.notNull<ImageView>()
    private var playWish by Delegates.notNull<ImageView>()
    constructor(ctx : Context) : super(ctx) {
        verticalLayout {
            orientation = LinearLayout.HORIZONTAL
            playWish = imageView {
                id = R.id.play_wish
                setImageResource(R.mipmap.play_icn_love)
            }.lparams(wrapContent, wrapContent)
            playInfo = imageView {
                setImageResource(R.mipmap.play_icn_more)
            }.lparams(wrapContent, wrapContent)
        }.lparams(wrapContent, wrapContent) {
            centerInParent()
        }
    }
}