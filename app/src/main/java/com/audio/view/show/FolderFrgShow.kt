package com.audio.view.show

import android.support.v4.app.Fragment
import android.view.View
import com.audio.view.layout.FolderFrgLayout
import com.audio.view.life.FrgLife
import org.jetbrains.anko.AnkoContext

class FolderFrgShow : FrgLife {

    override fun onCreateView(context: Fragment, any: Any?): Any {
        return initView(context)
    }

    fun initView(fragment: Fragment): View {
        return FolderFrgLayout().createView(AnkoContext.create(fragment.context, fragment))
    }
}

