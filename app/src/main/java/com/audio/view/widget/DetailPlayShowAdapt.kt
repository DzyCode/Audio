package com.audio.view.widget

import android.content.Context
import android.net.Uri
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.audio.util.execute

class DetailPlayShowAdapt : PagerAdapter() {

    private var datas = mutableListOf<Uri>()
    private var views = mutableListOf<AlbumCover>()
    private var isChanged = false
    fun setDatas(ctx: Context, mutableList: MutableList<Uri>) {
        isChanged = datas.size == mutableList.size
        datas.clear()
        mutableList.forEachIndexed { index, uri ->
            (index < views.size).execute({
                views[index].load(uri)
                views[index].rotation = 0F
            },{
                views.add(AlbumCover(ctx))
                views[index].load(uri)
            })
        }
        datas.addAll(mutableList)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return datas.size
    }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(views[position])
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any{
        container?.addView(views[position])
        return views[position]
    }

    override fun getItemPosition(`object`: Any?): Int {
        return if (!isChanged) POSITION_NONE else POSITION_UNCHANGED
    }
}
