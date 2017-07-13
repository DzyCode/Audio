package com.audio.view.show

import android.app.Activity
import android.support.design.widget.TabLayout
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import audio.com.audio.R
import com.audio.play.SongQueueManager
import com.audio.present.DefaultPresent
import com.audio.present.base.ICallBack
import com.audio.util.*
import com.audio.util.agent.currentSong
import com.audio.view.fragment.FragmentsAdapt
import com.audio.view.fragment.createFragmentItems
import com.audio.view.fragment.createTableNames
import com.audio.view.layout.CollectionLayout
import com.audio.view.life.AtyLife
import com.audio.view.widget.PlayBar
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import kotlin.properties.Delegates

class CollectionShow : AtyLife, ICallBack {
    var parentView by Delegates.notNull<View>()
    var context by Delegates.notNull<Activity>()
    val tableLayout: TabLayout by lazy { parentView.find<TabLayout>(R.id.head_tablayout) }
    val viewPage: ViewPager by lazy { parentView.find<ViewPager>(R.id.content_viewpage) }
    val toolBar: Toolbar by lazy { parentView.find<Toolbar>(R.id.head_bar) }
    val playBar: PlayBar by lazy { PlayBar(parentView, context) }
    lateinit var present: DefaultPresent

    override fun receive(): (Activity, LifeOrder, Any?) -> Any {
        return {
            activity, lifeOrder, any ->
            when (lifeOrder) {
                LifeOrder.ONCREATE -> onCreate(activity)
                LifeOrder.ONDESTROY -> onDestroy()
            }
        }
    }

    private fun onCreate(activity: Activity) {
        context = activity
        initView(activity.to<AppCompatActivity>())
        initVariable(activity)
    }

    private fun initView(activity: AppCompatActivity) {
        parentView = CollectionLayout().setContentView(activity)
        activity.setSupportActionBar(toolBar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewPage.adapter = FragmentsAdapt(activity.supportFragmentManager, createFragmentItems(), createTableNames())
        tableLayout.setupWithViewPager(viewPage)
        playBar.play(currentSong())
    }

    private fun initVariable(activity: Activity) {
        present = DefaultPresent(activity)
        present.registerCallback(this)
        present.connect()
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