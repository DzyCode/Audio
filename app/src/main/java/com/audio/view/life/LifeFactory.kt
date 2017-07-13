package com.audio.view.life

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import com.audio.util.LifeOrder
import com.audio.util.to
import com.audio.view.AudioActivity
import com.audio.view.fragment.AudioFragment
import com.audio.view.show.*

enum class Show {
    AUDIOSHOW, LOCALMUSICSHOW, RECENTLYMUSICSHOW, DETAILSPLAYSHOW, COLLECTIONSHOW
}

enum class AudioFrgToken {
    SONG, SINGER, ALBUM, FOLDER, SONGCOLLECTION, ALBUMCOLLECTION
}

fun Context.showToken(): String {
    return "AudioActivityShow"
}

fun Context.dataToken() : String {
    return "extras"
}
fun Context.playStateToken() : String {
    return "playbackstate"
}

fun AudioActivity.createLife(show: Show): (Activity, LifeOrder, Any?) -> Any {
    when (show) {
        Show.AUDIOSHOW -> return AudioShow().to<AtyLife>().receive()
        Show.LOCALMUSICSHOW -> return LocalMusicShow().to<AtyLife>().receive()
        Show.RECENTLYMUSICSHOW -> return RecentlyPlayShow().to<AtyLife>().receive()
        Show.DETAILSPLAYSHOW -> return DetailsPlayShow().to<AtyLife>().receive()
        Show.COLLECTIONSHOW -> return CollectionShow().to<AtyLife>().receive()
    }
    return AudioShow().to<AtyLife>().receive()
}

fun AudioFragment.showToken() : String {
    return "AudioFragment"
}

fun AudioFragment.createLife(token : AudioFrgToken) : (Fragment, LifeOrder, Any?) -> Any {
    when(token) {
        AudioFrgToken.SONG -> return SongFrgShow().to<FrgLife>().receive()
        AudioFrgToken.SINGER -> return SingerFrgShow().to<FrgLife>().receive()
        AudioFrgToken.ALBUM -> return AlbumFrgShow().to<FrgLife>().receive()
        AudioFrgToken.FOLDER -> return FolderFrgShow().to<FrgLife>().receive()
        AudioFrgToken.SONGCOLLECTION -> return SongCollectionFrgShow().to<FrgLife>().receive()
        AudioFrgToken.ALBUMCOLLECTION -> return FolderFrgShow().to<FrgLife>().receive()
    }
}
