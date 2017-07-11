package com.audio.view.life

import android.support.v4.app.Fragment
import com.audio.util.LifeOrder

abstract class FrgLife : ILife<Fragment> {
    abstract override fun receive(): (Fragment, LifeOrder, Any?) -> Any
}

