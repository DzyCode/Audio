package com.audio.util

import android.net.Uri
import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.ImageView
import com.audio.AudioApp
import com.audio.model.RecentlyPlayList
import com.audio.model.Song
import com.bumptech.glide.Glide
import com.db.recycler.RcyList
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <U> Any.to(): U {
    return this as U
}

fun <T : Number> T.toMb(): String {
    return toDouble().div(1024 * 1024).decimal(2).toString() + " M"
}

fun Double.decimal(digts: Int): Double {
    var result: Double = 1.toDouble()
    for (i in 0 until digts) {
        result *= 10
    }
    return this.times(result).toInt().toDouble().div(result)
}

class NotNullReadOnlySingleValue<T> : ReadOnlyProperty<Any?, T> {
    private var value: T? = null
    private var single: () -> T

    constructor(single: () -> T) {
        this.single = single
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value == null) {
            value = single.invoke()
        }
        return value!!
    }
}

class NotNullSingleValue<T> : ReadWriteProperty<Any?, T> {
    private var value: T? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalArgumentException("${property.name} has not been initialized")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value else throw IllegalArgumentException("${property.name} has already initilized")
    }
}

fun <T, E> T.letNotNull(e: E?, block: E.(T) -> Unit) {
    e?.block(this)
}

fun <T> RcyList.filter(block: (Any) -> T?): MutableList<T> {
    val result = mutableListOf<T>()
    forEach {
        block.invoke(it)?.let {
            result.add(it)
        }
    }
    return result
}

fun <T, R, E : List<R>> E.filter(block : (R) -> T?) : MutableList<T> {
    val result = mutableListOf<T>()
    forEach { block.invoke(it)?.let { result.add(it) } }
    return result
}
fun <T : Any, E> T.equals(isEuqal: Boolean, e: E, block: (T, E) -> Unit) {
    if (this.equals(e) == isEuqal) {
        block.invoke(this, e)
    }
}

fun Song.toRecentlyPlayList(time: Long? = null): RecentlyPlayList {
    return RecentlyPlayList(_id, time ?: System.currentTimeMillis())
}

fun <T : ImageView> T.load(uri: String) {
    Glide.with(AudioApp.instance)
            .load(uri)
            .into(this)
}

fun <T : ImageView> T.load(uri: Uri) {
    Glide.with(AudioApp.instance)
            .load(uri)
            .into(this)
}

fun PlaybackStateCompat.playing(block: () -> Unit) {
    if (this.state == PlaybackStateCompat.STATE_PLAYING) {
        block.invoke()
    }
}

fun PlaybackStateCompat.paused(block: () -> Unit) {
    if (this.state == PlaybackStateCompat.STATE_PAUSED) {
        block.invoke()
    }
}

fun PlaybackStateCompat.currentPlayProgress(): Long {
    val delt = SystemClock.elapsedRealtime() - this.lastPositionUpdateTime
    return (delt * this.playbackSpeed + this.position).toLong()
}

fun <T : Number> T.MMSS(): String {
    val value = this.toLong() / 1000  //ms -> s
    if (value >= 3660) {
        return "59:59"
    } else {
        val ss = value % 60
        val mm = value / 60
        return "${mm.fillOneZero()}:${ss.fillOneZero()}"
    }
}

fun <T : Number> T.fillOneZero(): String {
    return if (this.toInt() <= 9) "0${this.toInt()}" else this.toString()
}

fun <T : Number> T.positive(block: (T) -> Unit) {
    if (this.toInt() > 0) {
        block.invoke(this)
    }
}

fun <T : Number> T.zero(block: (T) -> Unit) {
    if (this.toInt() == 0) {
        block.invoke(this)
    }
}

fun <T : Number> T.negative(block: (T) -> Unit) {
    if (this.toInt() < 0) {
        block.invoke(this)
    }
}

fun Boolean.yes(block: () -> Unit) {
    if (this) {
        block.invoke()
    }
}

fun Boolean.execute(yesBlock: () -> Unit, noBlock: () -> Unit) {
    if (this) yesBlock.invoke() else noBlock.invoke()
}

fun Boolean.no(block: () -> Unit) {
    if (!this) {
        block.invoke()
    }
}

