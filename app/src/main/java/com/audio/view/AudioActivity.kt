package com.audio.view

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.audio.present.base.ICallBack
import com.audio.util.LifeOrder
import com.audio.util.emit
import com.audio.util.to
import com.audio.view.life.Show
import com.audio.view.life.createLife
import com.audio.view.life.showToken

class AudioActivity : AppCompatActivity() {

    lateinit var receive : (Activity, LifeOrder, Any?) -> Any
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var token = intent.getSerializableExtra(showToken()) ?: Show.AUDIOSHOW
        receive = createLife(token.to())
        emit(LifeOrder.ONCREATE, savedInstanceState, receive)
    }

    override fun onStart() {
        super.onStart()
        emit(LifeOrder.ONSTART, null, receive)
    }

    override fun onResume() {
        super.onResume()
        emit(LifeOrder.ONRESUME, null, receive)
    }

    override fun onStop() {
        super.onStop()
        emit(LifeOrder.ONSTOP, null, receive)
    }

    override fun onDestroy() {
        super.onDestroy()
        emit(LifeOrder.ONDESTROY, null, receive)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        emit(LifeOrder.ONCREATEOPTIONSMENU, menu, receive)
        return true
    }
}
