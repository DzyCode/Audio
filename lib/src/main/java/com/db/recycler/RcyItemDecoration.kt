package com.db.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.properties.Delegates

class RcyItemDecoration : RecyclerView.ItemDecoration {

    private var isVertical : Boolean
    var attr = intArrayOf(android.R.attr.listDivider)
    var drawable by Delegates.notNull<Drawable>()

    constructor(context : Context, vertical : Boolean = true) : super() {
        val array = context.obtainStyledAttributes(attr)
        drawable = array.getDrawable(0)
        array.recycle()
        isVertical = vertical
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        if(isVertical)  {
            outRect?.set(0, 0, 0, drawable.intrinsicHeight)
        } else{
            outRect?.set(0, 0, drawable.intrinsicWidth, 0)
        }
    }

    override fun onDraw(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        if (isVertical) {
            drawVertical(c, parent, state)
        }
    }

    private fun drawVertical(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        parent?.let {
            val left = it.paddingLeft
            val right = it.width - parent.paddingRight
            for(i in 0 until it.childCount) {
                val child = it.getChildAt(i)
                val layout = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + layout.bottomMargin
                drawable.setBounds(left, top, right, top + drawable.intrinsicHeight)
                drawable.draw(c)
            }
        }
    }
}

