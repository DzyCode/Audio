package com.audio.view.life

import android.app.Activity

interface AtyLife : ILife<Activity> {
    override fun  receive(): (android.app.Activity, com.audio.util.LifeOrder, Any?) -> Any
}

