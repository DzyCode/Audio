package com.audio.view.widget

import android.content.Context
import android.net.Uri
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.widget.ImageView
import audio.com.audio.R
import com.audio.model.picUri
import com.audio.play.SongQueueManager
import com.audio.present.base.IPlayControlCallback
import com.audio.util.*
import com.audio.util.agent.canPlayNext
import com.audio.util.agent.canPlayPre
import com.audio.util.agent.currentSong
import com.audio.view.AudioActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.viewPager

class DetailPlayShowView : _RelativeLayout {

    companion object {
        private val ROTATION_TIME = 0.2 * 1000
        private val NEEDLE_ANGLE = -30F
    }

    private val needle by lazy { find<ImageView>(R.id.play_show_needle) }
    private val viewpage by lazy { find<ViewPager>(R.id.play_show_page) }
    private val pageAdapt by lazy { DetailPlayShowAdapt() }
    private val fragmentmanager: FragmentManager
    private val speed = 10

    private var playControlCallback: IPlayControlCallback? = null
    private var isRotationReset = true
    private var isFromOutside = false
    private var currentPage: Int = -1
    private var beginRotationTime = -1L

    private val rotationPlayingCallback: (Long) -> Unit = {
        rotation(true, System.currentTimeMillis() - beginRotationTime)
    }
    private val rotationPauseCallback: (Long) -> Unit = {
        rotation(false, System.currentTimeMillis() - beginRotationTime)
    }

    constructor(ctx: Context) : super(ctx) {
        relativeLayout {
            relativeLayout {
                imageView {
                    id = R.id.play_show_bg
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    setImageResource(R.mipmap.play_disc_halo)
                }.lparams(dip(263), dip(263)) {
                    centerHorizontally()
                    setMargins(0, dip(60), 0, 0)
                }
                viewPager {
                    id = R.id.play_show_page
                }.lparams(dip(263), dip(263)) {
                    centerHorizontally()
                    setMargins(0, dip(60), 0, 0)
                }
                imageView {
                    id = R.id.play_show_needle
                    setImageResource(R.mipmap.play_needle)
                    pivotX = dip(15.1.toFloat()).toFloat()
                    pivotY = dip(15.1.toFloat()).toFloat()
                    rotation = -30.toFloat()
                }.lparams(wrapContent, wrapContent) {
                    alignParentTop()
                    alignEnd(R.id.play_show_bg)
                    setMargins(0, dip(-10), dip(50), 0)
                }
            }.lparams(wrapContent, wrapContent) {
                centerHorizontally()
            }
            imageView {
                setImageResource(R.mipmap.play_topbar_line)
            }.lparams(matchParent, wrapContent) {
                alignParentTop()
            }
        }.lparams(matchParent, wrapContent)
        fragmentmanager = ctx.to<AudioActivity>().supportFragmentManager
        initView()
    }

    private fun initView() {
        viewpage.adapter = pageAdapt
        viewpage.addOnPageChangeListener(PageListener())
    }

    fun registerPresent(icallback: IPlayControlCallback) {
        this.playControlCallback = icallback
    }

    fun playNext() {
        canPlayNext().yes {
            isFromOutside = true
            viewpage.setCurrentItem(viewpage.currentItem + 1, true)
        }
    }

    fun playPre() {
        canPlayPre().yes {
            isFromOutside = true
            viewpage.setCurrentItem(viewpage.currentItem - 1, true)
        }
    }

    fun pause() {
        beginRotation(false)
    }

    fun play() {
        beginRotation(true)

    }

    private fun beginRotation(isPlay: Boolean) {
        (isPlay != isRotationReset).yes {
            endRotation(true)
            endRotation(false)
            beginRotationTime = System.currentTimeMillis()
            isPlay.execute({
                ChoreographerCompat.instance.registerCallback(rotationPlayingCallback)
            }, {
                ChoreographerCompat.instance.registerCallback(rotationPauseCallback)
            })
            isRotationReset = isPlay
        }
    }

    private fun endRotation(isPlay: Boolean) {
        beginRotationTime = -1
        isPlay.execute({
            ChoreographerCompat.instance.unRegisterCallback(rotationPlayingCallback)
        }, {
            ChoreographerCompat.instance.unRegisterCallback(rotationPauseCallback)
        })

    }

    private fun rotation(isPlay: Boolean, duration: Long) {
        (ROTATION_TIME - duration >= 0).execute({
            isPlay.execute({
                val angle = NEEDLE_ANGLE * (1 - duration / ROTATION_TIME)
                needle.rotation = angle.toFloat()
            }, {
                val angle = NEEDLE_ANGLE * duration / ROTATION_TIME
                needle.rotation = angle.toFloat()
            })
        }, {
            endRotation(isPlay)
        })
    }

    fun updatePlayState(isPlaying: Boolean) {
        isRotationReset = isPlaying
        updateNeedlaAngle(if (isPlaying) 0F else NEEDLE_ANGLE)
        viewpage.currentItem = setCurrentDatas()
        currentPage = viewpage.currentItem
    }

    private fun setCurrentDatas(): Int {
        val datas = mutableListOf<Uri>()
        var currentIndex = 0
        SongQueueManager.instance.getPreSong()?.let {
            datas.add(it.picUri())
            currentIndex = 1
        }
        SongQueueManager.instance.getCurrentSong()?.let { datas.add(it.picUri()) }
        SongQueueManager.instance.getNextSong()?.let {
            datas.add(it.picUri())
            currentIndex = if (currentIndex == 1) 1 else 0
        }
        pageAdapt.setDatas(context, datas)
        pageAdapt.notifyDataSetChanged()
        return currentIndex
    }

    private fun updateNeedlaAngle(angle: Float) {
        needle.rotation = angle
    }

    fun updateAlbumAngle(progress: Long) {
        viewpage.forEachChild {
            (it.to<AlbumCover>().imageUri == currentSong()?.picUri()).yes {
                it.rotation = progress * speed / 1000.toFloat()
            }
        }
    }

    inner class PageListener : ViewPager.OnPageChangeListener {
        private var isDragging = false
        override fun onPageScrollStateChanged(state: Int) {
            (state == ViewPager.SCROLL_STATE_DRAGGING).yes {
                beginRotation(false)
                isDragging = true
            }
            (state == ViewPager.SCROLL_STATE_SETTLING).yes {
                beginRotation(false)
            }
            (state == ViewPager.SCROLL_STATE_IDLE).yes {
                beginRotation(isDragging || isPlaying())
                isDragging = false
                (isFromOutside && currentPage != viewpage.currentItem).yes {
                    viewpage.setCurrentItem(setCurrentDatas(), false)
                    currentPage = viewpage.currentItem
                }
                isFromOutside = false
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            (!isFromOutside && position != currentPage).yes {
                (position > currentPage).execute({
                    playNext()
                }, {
                    playPre()
                })
            }
        }

        private fun playNext() {
            playControlCallback?.playNext()
        }

        private fun playPre() {
            playControlCallback?.playPre()
        }

        private fun isPlaying(): Boolean {
            var result = false
            playControlCallback?.getPlaybackState()?.playing { result = true }
            return result
        }
    }
}
