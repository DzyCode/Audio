package com.audio.view.show

import android.support.v4.app.Fragment
import android.view.View
import com.audio.util.LifeOrder
import com.audio.view.layout.FolderFrgLayout
import com.audio.view.life.FrgLife
import org.jetbrains.anko.AnkoContext

class FolderFrgShow : FrgLife() {
    override fun receive(): (Fragment, LifeOrder, Any?) -> Any {
        return {
            fragment, lifeOrder, any ->
            when(lifeOrder) {
                LifeOrder.ONCREATEVIEW -> {
                    initView(fragment)
                }
                else -> Any()
            }
        }
    }

    fun initView(fragment: Fragment) : View {
        return FolderFrgLayout().createView(AnkoContext.create(fragment.context, fragment))
    }
}

