package com.audio.view.widget

import android.content.Context
import android.net.Uri
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.audio.model.picUri
import com.audio.play.SongQueueManager
import com.audio.util.execute
import com.audio.util.log
import com.audio.util.no
import com.audio.util.yes

class DetailPlayShowAdapt : PagerAdapter() {

    private var datas = mutableListOf<Uri>()
    private var views = mutableListOf<AlbumCover>()
    private var isChanged = false
    fun setDatas(ctx: Context, mutableList: MutableList<Uri>) {
        isChanged = datas.size == mutableList.size
        datas.clear()
        isChanged.execute({
            for(i in 0 until views.size) {
                views[i].load(mutableList[i])
                views[i].rotation = 0F
            }
        },{
            views.clear()
            mutableList.forEach {
                var albumcover = AlbumCover(ctx)
                albumcover.load(it)
                views.add(albumcover)
            }
        })
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
        container?.let {
            it.removeView(views.get(position))
        }
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any{
        container?.let {
            it.addView(views[position])
        }
        return views[position]
    }

    override fun getItemPosition(`object`: Any?): Int {
        return if (!isChanged) POSITION_NONE else POSITION_UNCHANGED
    }
}
