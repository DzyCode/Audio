package com.audio.view.fragment

import android.support.v4.app.Fragment
import com.audio.view.life.AudioFrgToken

fun LocalFragmentAdapt.createFragmentItems() : MutableList<Fragment> {
    return mutableListOf(AudioFragment.newInstance(AudioFrgToken.SONG),
            AudioFragment.newInstance(AudioFrgToken.SINGER),
            AudioFragment.newInstance(AudioFrgToken.ALBUM),
            AudioFragment.newInstance(AudioFrgToken.FOLDER))
}

fun LocalFragmentAdapt.createTableNames() : MutableList<CharSequence> {
    return mutableListOf("单曲","歌手","专辑","文件夹")
}
