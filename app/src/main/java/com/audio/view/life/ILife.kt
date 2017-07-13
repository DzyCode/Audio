package com.audio.view.life

import com.audio.util.LifeOrder

interface ILife <T>{
    fun receive() : (T, LifeOrder, Any?) -> Any
}