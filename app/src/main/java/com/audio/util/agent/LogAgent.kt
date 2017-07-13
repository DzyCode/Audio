package com.audio.util.agent

import android.util.Log
import com.audio.util.yes

fun <T : Any> T.execute(block : () -> Unit) {
    true.yes { block.invoke() }
}

fun <T : Any> T.logd(tag: String, subtag: String) {
    execute { Log.d("dbcom_$tag", subtag) }
}

fun <T : Any> T.logd(any: Any, subtag: String) {
    logd(any.javaClass.name, subtag)
}

