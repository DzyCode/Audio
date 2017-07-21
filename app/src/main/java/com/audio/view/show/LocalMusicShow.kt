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
import com.audio.present.DefaultPresent
import com.audio.present.base.ICallBack
import com.audio.util.*
import com.audio.util.agent.currentSong
import com.audio.view.fragment.FragmentsAdapt
import com.audio.view.fragment.createFragmentItems
import com.audio.view.fragment.createTableNames
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import kotlin.properties.Delegates

class LocalMusicShow : AtyLife, ICallBack {

    var parentView by Delegates.notNull<View>()
    var context by Delegates.notNull<Activity>()
    val tableLayout: TabLayout by lazy { parentView.find<TabLayout>(R.id.head_tablayout) }
    val viewPage: ViewPager by lazy { parentView.find<ViewPager>(R.id.content_viewpage) }
    val toolBar: Toolbar by lazy { parentView.find<Toolbar>(R.id.head_bar) }
    val playBar: PlayBar by lazy { PlayBar(parentView, context) }
    lateinit var present: DefaultPresent

    override fun onCreate(context: Activity, any: Any?) {
        initView(context)
        initVariable(context)
    }

    private fun initView(activity: Activity) {
        parentView = LocalMusicLayout().setContentView(activity)
        activity.to<AppCompatActivity>().setSupportActionBar(toolBar)
        activity.to<AppCompatActivity>().supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewPage.adapter = FragmentsAdapt(activity.to<AppCompatActivity>().supportFragmentManager,
                createFragmentItems(), createTableNames())
        tableLayout.setupWithViewPager(viewPage)
    }

    private fun initVariable(activity: Activity) {
        context = activity
        present = DefaultPresent(activity)
        present.registerCallback(this)
        present.connect()
        toolBar.setNavigationOnClickListener {
            context.onBackPressed()
        }
    }

    override fun onStart() {
        playBar.play(currentSong())
    }

    override fun onDestroy() {
        present.disconnect()
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
        state?.let {
            it.playing { playBar.play(SongQueueManager.instance.getCurrentSong()) }
            it.paused { playBar.pause() }
        }
    }
}

