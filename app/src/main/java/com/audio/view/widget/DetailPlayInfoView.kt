package com.audio.view.widget

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import audio.com.audio.R
import com.audio.model.Song
import com.audio.model.node.Node
import com.audio.util.*
import com.audio.util.agent.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk15.coroutines.onClick
import kotlin.properties.Delegates

class DetailPlayInfoView : _RelativeLayout {
    private var playInfo by Delegates.notNull<ImageView>()
    private var playWish by Delegates.notNull<ImageView>()
    private var isCollection: Boolean = false
    private var isCanMark = false

    constructor(ctx: Context) : super(ctx) {
        verticalLayout {
            orientation = LinearLayout.HORIZONTAL
            playWish = imageView {
                id = R.id.play_wish
                onClick {
                    isCollection.execute({
                        unMarkCurrentSongToCollection()
                    }, {
                        markCurrentSongToCollection()
                    })
                    updatePlayState(currentSong())
                }
            }.lparams(wrapContent, wrapContent)
            playInfo = imageView {
                setImageResource(R.mipmap.play_icn_more)
            }.lparams(wrapContent, wrapContent)
        }.lparams(wrapContent, wrapContent) {
            centerInParent()
        }
        updatePlayState(currentSong())
    }

    fun setQueueTitle(title: String?) {
        logd(this.javaClass.name, "queue title is: $title")
        title?.let { isCanMark = it == Node.SONGCOLLECTION }
    }

    fun updatePlayState(song: Song?) {
        song?.let {
            isCollection = isMarkedSongToCollection(it._id)
            playWish.setImageResource(
                    if (isCollection) R.mipmap.play_icn_loved else R.mipmap.play_icn_love)
        }
    }
}