package com.audio.view.life

import com.audio.util.LifeOrder

interface ILife <T : Any>{
    fun receive() : (T, LifeOrder, Any?) -> Any
}