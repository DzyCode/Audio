package com.audio.view.show

import android.app.Activity
import android.support.design.widget.AppBarLayout
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import audio.com.audio.R
import com.audio.model.AudioHeadItem
import com.audio.model.AudioSettingItem
import com.audio.view.widget.PlayBar
import com.audio.model.node.Node
import com.audio.play.SongQueueManager
import com.audio.present.AudioPresent
import com.audio.present.base.ICallBack
import com.audio.view.layout.*
import com.audio.view.life.AtyLife
import com.audio.util.LifeOrder
import com.audio.util.startActivity
import com.audio.util.to
import com.audio.view.AudioActivity
import com.audio.view.life.Show
import com.db.recycler.RcyList
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.jetbrains.anko.setContentView
import kotlin.properties.Delegates

class AudioShow : AtyLife, ICallBack {
    var parent by Delegates.notNull<View>()
    val toolbar: Toolbar by lazy { parent.find<Toolbar>(R.id.toobar) }
    val drawlayout by lazy { parent.find<DrawerLayout>(R.id.drawlayout) }
    val appbar by lazy { parent.find<AppBarLayout>(R.id.appbar) }
    val audioList: RecyclerView by lazy { parent!!.find<RecyclerView>(R.id.audio_list) }
    val playBar: PlayBar by lazy { PlayBar(parent, context) }
    lateinit var audioData: RcyList
    lateinit var context: Activity

    var audioHeadBind: (View?, Any) -> Unit = {
        view: View?, any: Any ->
        view?.let {
            var tag = it.tag as NavigationHead? ?: NavigationHead(it)
            var data = any as AudioHeadItem
            tag.audioText.text = data.title + "(" + data.numSongs + ")"
            tag.audioIcon.setImageResource(data.icon)
            it.tag = tag
            it.onClick {
                startNewActivity(data.showType)
            }
        }
    }
    var audioHeadView: (LayoutInflater, ViewGroup?) -> View = {
        layoutInflater: LayoutInflater, viewGroup: ViewGroup? ->
        viewGroup!!.context.createAudioHead()
    }

    var audioSettingBind: (View?, Any) -> Unit = {
        view, any ->
        var tag = view!!.tag as NavigationSetting? ?: NavigationSetting(view)
        var data = any.to<AudioSettingItem>()
        tag.audioPlyName.text = data.title + "(" + data.numItem + ")"
        tag.audioPlyExpand.setImageResource(data.icon)
        view!!.tag = tag
    }
    var audioSettingView: (LayoutInflater, ViewGroup?) -> View = {
        layoutInflater, viewGroup ->
        viewGroup!!.context.createAudioSettingsItem()
    }
    lateinit var actionBarToggle: ActionBarDrawerToggle
    lateinit var audioPresent: AudioPresent

    var receive: (Activity, LifeOrder, Any?) -> Any = {
        activity, lifeOrder, any ->
        when (lifeOrder) {
            LifeOrder.ONCREATE -> {
                initView(activity)
                initVariable(activity)
            }
            LifeOrder.ONSTART -> {
                onStart()
            }
            LifeOrder.ONSTOP -> {
                onStop()
            }
            LifeOrder.ONCREATEOPTIONSMENU -> {
                activity.to<AppCompatActivity>().menuInflater.inflate(R.menu.search, any!!.to())
            }
            LifeOrder.ONRESUME -> onResume()
            LifeOrder.ONDESTROY -> onDestroy()
        }
    }

    override fun receive(): (Activity, LifeOrder, Any?) -> Any {
        return receive
    }

    fun initView(activity: Activity) {
        parent = AudioLayout().setContentView(activity)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        actionBarToggle = ActionBarDrawerToggle(activity, drawlayout, toolbar, R.string.open, R.string.close)
        drawlayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()

        audioData = RcyList()
        audioData.registerType(AudioHeadItem::class.java, audioHeadBind, audioHeadView)
        audioData.registerType(AudioSettingItem::class.java, audioSettingBind, audioSettingView)
        audioList.adapter = audioData.adapt()
    }

    fun initVariable(activity: Activity) {
        this.context = activity
        audioPresent = AudioPresent(activity)
        audioPresent.registerCallback(this)
        audioPresent.connect()
        updatePlayBar(true)
    }

    fun onStart() {
        audioPresent.loadDataWithId<Any>(Node.NAVIGATION, {
            id, list ->
            audioData.clear()
            for (item in list) {
                when (item) {
                    is AudioHeadItem -> audioData.add(item.to<AudioHeadItem>())
                    is AudioSettingItem -> audioData.add(item.to<AudioSettingItem>())
                }
            }
            audioData.notifyDataSetChanged()
        })
    }

    fun onResume() {
        updatePlayBar(false)
    }

    fun onStop() {
    }

    fun onDestroy() {
        audioPresent.disconnect()
    }

    fun startNewActivity(show: Show) {
        context.to<AudioActivity>().startActivity(show)
    }

    fun updatePlayBar(withCurrentSong: Boolean) {
        if (withCurrentSong) playBar.play(SongQueueManager.instance.getCurrentSong()) else playBar.update()
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
        state?.let {
            when (it.state) {
                PlaybackStateCompat.STATE_PLAYING -> {
                    playBar.play(SongQueueManager.instance.getCurrentSong())
                }
                PlaybackStateCompat.STATE_PAUSED -> playBar.pause()
            }
        }
    }
}

