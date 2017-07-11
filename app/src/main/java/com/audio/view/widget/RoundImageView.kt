package com.audio.view.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import com.audio.AudioApp
import com.audio.util.to
import kotlin.properties.Delegates


class RoundImageView : ImageView {
    private var paint: Paint by Delegates.notNull()
    private var center: Int = 0
    private var radius: Int = 0

    constructor(context: Context = AudioApp.instance, attr: AttributeSet? = null, style: Int = 0)
            : super(context, attr, style) {
        paint = Paint()
        paint.isAntiAlias = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        center = Math.min(w, h) / 2
        radius = center
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        updatePaint()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        updatePaint()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        updatePaint()
    }

    private fun updatePaint() {
        if (drawable is BitmapDrawable) {
            paint.shader = BitmapShader(drawable.to<BitmapDrawable>().bitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.drawCircle(center.toFloat(), center.toFloat(), radius.toFloat(), paint)
        }
    }
}