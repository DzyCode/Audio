package com.audio.view.life

enum class LifeOrder{
    ONCREATEVIEW, ONCREATE, ONSTART, ONRESUME, ONSTOP, ONPAUSE, ONDESTROY, ONCREATEOPTIONSMENU
}

interface ILife <T>{
    fun receive() : (T, LifeOrder, Any?) -> Any
}