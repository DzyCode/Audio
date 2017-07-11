package com.audio.view.widget

import android.content.Context
import android.net.Uri
import android.widget.FrameLayout
import android.widget.ImageView
import audio.com.audio.R
import com.audio.util.load
import org.jetbrains.anko.*
import kotlin.properties.Delegates

class AlbumCover : _FrameLayout {
    private var imageview by Delegates.notNull<ImageView>()
    var imageUri : Uri? = null

    constructor(ctx: Context, uri: Uri? = null) : super(ctx) {
        layoutParams = FrameLayout.LayoutParams(dip(250), dip(250))
        imageview = imageView {
            scaleType = ImageView.ScaleType.FIT_CENTER
        }.lparams(matchParent, matchParent) {
            margin = dip(45)
        }
        uri?.let {
            load(it)
        }
        imageView {
            setBackgroundResource(R.mipmap.play_disc)
        }.lparams(matchParent, matchParent)
    }

    fun load(uri: Uri) {
        imageUri = uri
        imageview.load(uri)
    }
}