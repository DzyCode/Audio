package com.audio.util.agent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.audio.view.AudioActivity
import com.audio.view.life.LifeOrder
import com.audio.view.life.Show
import com.audio.view.life.dataToken
import com.audio.view.life.showToken


fun <T : Context> T.emit(order: LifeOrder, data: Any?, receive: (T, LifeOrder, Any?) -> Any): Any {
    return receive.invoke(this, order, data)
}

fun <T : Fragment> T.emit(order: LifeOrder, data: Any?, receive: (T, LifeOrder, Any?) -> Any): Any {
    return receive.invoke(this, order, data)
}

fun <T : Context> T.startActivity(show: Show, bundle: Bundle? = null) {
    val intent = Intent()
    intent.setClass(this, AudioActivity::class.java)
    intent.putExtra(showToken(), show)
    bundle?.let { intent.putExtra(dataToken(), it) }
    startActivity(intent)
}