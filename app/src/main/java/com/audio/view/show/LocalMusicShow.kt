package com.audio.view.show

import android.app.Activity
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import audio.com.audio.R
import com.audio.view.layout.LocalMusicLayout
import com.audio.view.life.AtyLife
import android.support.design.widget.TabLayout
import android.support.v4.media.session.PlaybackStateCompat
import com.audio.view.widget.PlayBar
import com.audio.play.SongQueueManager
import com.audio.present.LocalMusicPresent
import com.audio.present.base.ICallBack
import com.audio.util.*
import com.audio.view.fragment.LocalFragmentAdapt
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import kotlin.properties.Delegates

class LocalMusicShow : AtyLife, ICallBack {

    var parentView by Delegates.notNull<View>()
    var context by Delegates.notNull<Activity>()
    val tableLayout: TabLayout by lazy { parentView.find<TabLayout>(R.id.local_music_tablayout) }
    val viewPage: ViewPager by lazy { parentView.find<ViewPager>(R.id.local_music_viewpage) }
    val toolBar: Toolbar by lazy { parentView.find<Toolbar>(R.id.local_music_bar) }
    val playBar: PlayBar by lazy { PlayBar(parentView, context) }
    lateinit var present: LocalMusicPresent
    var receive: (Activity, LifeOrder, Any?) -> Any = {
        activity, lifeOrder, any ->
        when (lifeOrder) {
            LifeOrder.ONCREATE -> onCreate(activity)
            LifeOrder.ONSTART -> onStart()
            LifeOrder.ONRESUME -> onResume()
            LifeOrder.ONSTOP -> onStop()
            LifeOrder.ONDESTROY -> onDestroy()
        }
    }

    override fun receive(): (Activity, LifeOrder, Any?) -> Any {
        return receive
    }

    private fun onCreate(activity: Activity) {
        initView(activity)
        initVariable(activity)
    }

    private fun initView(activity: Activity) {
        parentView = LocalMusicLayout().setContentView(activity)
        activity.to<AppCompatActivity>().setSupportActionBar(toolBar)
        activity.to<AppCompatActivity>().supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewPage.adapter = LocalFragmentAdapt(activity.to<AppCompatActivity>().supportFragmentManager)
        tableLayout.setupWithViewPager(viewPage)
    }

    private fun initVariable(activity: Activity) {
        context = activity
        present = LocalMusicPresent(activity)
        present.registerCallback(this)
        present.connect()
        toolBar.setNavigationOnClickListener {
            context.onBackPressed()
        }
    }

    private fun onStart() {
        playBar.play(SongQueueManager.instance.getCurrentSong())
    }

    private fun onResume() {

    }

    private fun onStop() {
    }

    private fun onDestroy() {
        present.disconnect()
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
        state?.let {
            it.playing { playBar.play(SongQueueManager.instance.getCurrentSong()) }
            it.paused { playBar.pause() }
        }
    }
}

