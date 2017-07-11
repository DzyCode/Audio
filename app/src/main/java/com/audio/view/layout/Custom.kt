package com.audio.view.layout

import android.content.Context
import android.view.ViewManager
import android.widget.ImageView
import com.audio.view.widget.*
import org.jetbrains.anko.custom.ankoView

inline fun <T : ImageView> T.selector(normalResId : Int, pressResId : Int) {
    setImageResource(normalResId)
    setOnFocusChangeListener { view, b ->
        setImageResource(if(b) pressResId else normalResId)
    }
}

inline fun ViewManager.roundimageview(theme : Int = 0) = roundimageview(theme){}
inline fun ViewManager.roundimageview(theme : Int = 0, init : RoundImageView.() -> Unit)
        = ankoView({ctx: Context -> RoundImageView(ctx) }, theme, init)

inline fun ViewManager.detailplaycontrolview(theme: Int = 0) = detailplaycontrolview(theme) {}
inline fun ViewManager.detailplaycontrolview(theme: Int = 0, init: DetailPlayConrolView.() -> Unit)
        = ankoView({ctx -> DetailPlayConrolView(ctx) }, theme, init)

inline fun ViewManager.detailplayprogressview(theme: Int = 0) = detailplayprogressview(theme) {}
inline fun ViewManager.detailplayprogressview(theme: Int = 0, init: DetailPlayProgressView.() -> Unit)
        = ankoView({ctx -> DetailPlayProgressView(ctx) }, theme, init)

inline fun ViewManager.detailplayinfoview(theme: Int = 0) = detailplayinfoview(theme) {}
inline fun ViewManager.detailplayinfoview(theme: Int = 0, init: DetailPlayInfoView.() -> Unit)
        = ankoView({ctx -> DetailPlayInfoView(ctx) }, theme, init)

inline fun ViewManager.detailplayshowview(theme: Int = 0) = detailplayshowview(theme) {}
inline fun ViewManager.detailplayshowview(theme: Int = 0, init: DetailPlayShowView.() -> Unit)
        = ankoView({ctx -> DetailPlayShowView(ctx) }, theme, init)