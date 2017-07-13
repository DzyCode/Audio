package com.audio.view.fragment

import android.support.v4.app.Fragment
import com.audio.view.life.AudioFrgToken
import com.audio.view.show.CollectionShow
import com.audio.view.show.LocalMusicShow

fun LocalMusicShow.createFragmentItems() : MutableList<Fragment> {
    return mutableListOf(AudioFragment.newInstance(AudioFrgToken.SONG),
            AudioFragment.newInstance(AudioFrgToken.SINGER),
            AudioFragment.newInstance(AudioFrgToken.ALBUM),
            AudioFragment.newInstance(AudioFrgToken.FOLDER))
}

fun LocalMusicShow.createTableNames() : MutableList<CharSequence> {
    return mutableListOf("单曲","歌手","专辑","文件夹")
}

fun CollectionShow.createFragmentItems() : MutableList<Fragment> {
    return mutableListOf(AudioFragment.newInstance(AudioFrgToken.SONGCOLLECTION),
            AudioFragment.newInstance(AudioFrgToken.ALBUMCOLLECTION))
}

fun CollectionShow.createTableNames() : MutableList<CharSequence> {
    return mutableListOf("歌曲","专辑")
}

