package com.audio.view.layout

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import audio.com.audio.R
import com.audio.model.*
import com.audio.util.load
import com.audio.util.toMb
import org.jetbrains.anko.*

class NavigationHead(parent: View) {
    val audioIcon: ImageView by lazy { parent.find<ImageView>(R.id.audio_item_icon) }
    val audioText: TextView by lazy { parent.find<TextView>(R.id.audio_item_txt) }
}

fun Context.createAudioHead(): View {
    return with(this) {
        relativeLayout {
            lparams(matchParent, wrapContent) {
            }
            imageView {
                id = R.id.audio_item_icon
                scaleType = ImageView.ScaleType.FIT_XY
            }.lparams(dip(48), dip(48)) {
                centerVertically()
                setMargins(dip(3), dip(3),dip(5),dip(3))
            }

            textView {
                id = R.id.audio_item_txt
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
            }.lparams(wrapContent, wrapContent) {
                centerVertically()
                rightOf(R.id.audio_item_icon)
            }
            view {
                background = obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
                        .getDrawable(0)
            }.lparams(matchParent, wrapContent) {
                alignParentBottom()
                alignStart(R.id.audio_item_txt)
            }
        }
    }
}

class NavigationSetting(parent: View) {
    val audioPlyExpand by lazy { parent.find<ImageView>(R.id.audio_playlist_expand) }
    val audioPlySetting by lazy { parent.find<ImageView>(R.id.audio_playlist_setting) }
    val audioPlyName by lazy { parent.find<TextView>(R.id.audio_playlist_name) }
}

fun Context.createAudioSettingsItem(): View {
    return with(this) {
        relativeLayout {
            lparams(matchParent, wrapContent) {
                background = obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
                        .getDrawable(0)
            }
            imageView {
                id = R.id.audio_playlist_expand

            }.lparams(wrapContent, wrapContent) {
                alignParentLeft()
                centerVertically()
                setMargins(0, dip(10), 0, dip(10))
            }

            imageView {
                id = R.id.audio_playlist_setting
                imageResource = R.mipmap.list_icn_more
            }.lparams(wrapContent, wrapContent) {
                alignParentRight()
                centerVertically()
            }
            textView {
                id = R.id.audio_playlist_name
                setPadding(dip(4), 0, 0, 0)
            }.lparams(wrapContent, matchParent) {
                rightOf(R.id.audio_playlist_expand)
                centerVertically()
            }
        }
    }
}

class SongHeadView(parent: View) {
    val head by lazy { parent.find<RelativeLayout>(R.id.song_head) }
    val playIcon by lazy { parent.find<ImageView>(R.id.song_head_icon) }
    val playTitle by lazy { parent.find<TextView>(R.id.song_head_title) }
    val playSubIcon by lazy { parent.find<ImageView>(R.id.song_head_subicon) }
    val playSubTitle by lazy { parent.find<TextView>(R.id.song_head_multiselect) }
    fun bindDate(songHead: SongHead) {
        playIcon.setImageResource(songHead.icon)
        playTitle.text = playTitle.context.applicationContext.resources
                .getString(songHead.title, songHead.songCount)
        playSubIcon.setImageResource(songHead.settingIcon)
        playSubTitle.setText(songHead.settingTitle)
    }
}

fun Context.createSongHeadView(): View {
    return with(this, {
        relativeLayout {
            id = R.id.song_head
            lparams(matchParent, wrapContent)
            imageView {
                id = R.id.song_head_icon
                scaleType = ImageView.ScaleType.FIT_XY
            }.lparams(wrapContent, wrapContent) {
                alignParentLeft()
                setMargins(dip(4), dip(10), dip(5), dip(10))
            }

            textView {
                id = R.id.song_head_title
            }.lparams(wrapContent, wrapContent) {
                rightOf(R.id.song_head_icon)
                leftOf(R.id.song_head_subicon)
                centerVertically()
            }

            textView {
                id = R.id.song_head_multiselect
            }.lparams(wrapContent, wrapContent) {
                alignParentRight()
                centerVertically()
            }

            imageView {
                id = R.id.song_head_subicon
            }.lparams(wrapContent, wrapContent) {
                leftOf(R.id.song_head_multiselect)
                centerVertically()
            }
        }
    })
}

class SongItemView(parent: View) {
    val icon by lazy { parent.find<ImageView>(R.id.song_local_icon) }
    val title by lazy { parent.find<TextView>(R.id.song_local_title) }
    val size by lazy { parent.find<TextView>(R.id.song_local_size) }
    val artist by lazy { parent.find<TextView>(R.id.song_local_subtitle) }
    val settingIcon by lazy { parent.find<ImageView>(R.id.song_local_seting_icon) }
    lateinit var song: Song
    fun bindDate(song: Song) {
        this.song = song
        title.text = song.title
        size.text = song.size.toMb() + "  "
        artist.text = song.artist + " - " + song.album
        settingIcon.setImageResource(R.mipmap.list_icn_more)
        icon.load(song.picUri())
    }
}

fun Context.createSongItemView(): View {
    return with(this) {
        relativeLayout {
            lparams(matchParent, wrapContent)
            imageView {
                id = R.id.song_local_icon
            }.lparams(dip(56), dip(56)) {
                alignParentLeft()
                centerVertically()
                setMargins(dip(3), dip(3), dip(4), dip(3))
            }
            relativeLayout {
                textView {
                    id = R.id.song_local_title
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams(wrapContent, wrapContent) {
                    setMargins(dip(3), dip(4), 0, dip(4))
                    alignParentLeft()
                }

                textView {
                    id = R.id.song_local_size
                }.lparams(wrapContent, wrapContent) {
                    below(R.id.song_local_title)
                    alignStart(R.id.song_local_title)
                    setMargins(0, dip(3), 0, dip(4))
                }

                textView {
                    id = R.id.song_local_subtitle
                }.lparams(wrapContent, wrapContent) {
                    below(R.id.song_local_title)
                    rightOf(R.id.song_local_size)
                    setMargins(0, dip(3), 0, dip(4))
                }
            }.lparams(wrapContent, wrapContent) {
                rightOf(R.id.song_local_icon)
                centerVertically()
            }


            imageView {
                id = R.id.song_local_seting_icon
            }.lparams(wrapContent, wrapContent) {
                alignParentRight()
                centerVertically()
            }
        }
    }
}

class SingerItemView(parent: View) {
    val icon by lazy { parent.find<ImageView>(R.id.local_item_icon) }
    val title by lazy { parent.find<TextView>(R.id.local_item_title) }
    val subTitle by lazy { parent.find<TextView>(R.id.local_item_subtitle) }
    val setting by lazy { parent.find<ImageView>(R.id.local_item_setting) }
    fun bind(singer: Singer) {
        title.text = singer.title
        subTitle.text = singer.numSongs.toString() + " 首"
        singer.data?.let {
            icon.load(it)
        }
    }
}

class AlbumItemView(parent: View) {
    val icon by lazy { parent.find<ImageView>(R.id.local_item_icon) }
    val title by lazy { parent.find<TextView>(R.id.local_item_title) }
    val subTitle by lazy { parent.find<TextView>(R.id.local_item_subtitle) }
    val setting by lazy { parent.find<ImageView>(R.id.local_item_setting) }
    fun bind(album: Album) {
        title.text = album.title
        subTitle.text = album.numSongs.toString() + " 首" + "  " + album.artist
        icon.load(album.picUri())
    }
}

fun Context.createLocalItem(): View {
    return with(this) {
        relativeLayout {
            lparams(matchParent, wrapContent)

            imageView {
                id = R.id.local_item_icon
            }.lparams(dip(56), dip(56)) {
                alignParentLeft()
                centerVertically()
                setMargins(dip(3), dip(3), dip(4), dip(3))
            }
            verticalLayout {
                textView {
                    id = R.id.local_item_title
                }.lparams(wrapContent, wrapContent) {
                    setMargins(dip(5), dip(1), 0, dip(3))
                }

                textView {
                    id = R.id.local_item_subtitle
                }.lparams(wrapContent, wrapContent) {
                }
            }.lparams(wrapContent, wrapContent) {
                centerVertically()
                rightOf(R.id.local_item_icon)
                setMargins(dip(5), 0, 0, 0)
            }
            imageView {
                id = R.id.local_item_setting
                setImageResource(R.mipmap.list_icn_more)
            }.lparams(wrapContent, wrapContent) {
                centerVertically()
                alignParentRight()
                setMargins(0, 0, dip(5), 0)
            }
        }
    }
}

class RecentlyPlayItemView(parent: View) {
    val title by lazy { parent.find<TextView>(R.id.recently_play_title) }
    val subtitle by lazy { parent.find<TextView>(R.id.recently_play_subtitle) }
    val settingIcon by lazy { parent.find<ImageView>(R.id.recently_play_setting) }
    lateinit var song: Song
    fun bindDate(song: Song) {
        this.song = song
        title.text = song.title
        subtitle.text = song.artist + " - " + song.album
    }
}

fun Context.createRecentlyPlayItemView(): View {
    return with(this) {
        relativeLayout {
            lparams(matchParent, wrapContent)
            relativeLayout {
                textView {
                    id = R.id.recently_play_title
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams(wrapContent, wrapContent) {
                    setMargins(dip(5), dip(4), 0, dip(4))
                }

                textView {
                    id = R.id.recently_play_subtitle
                }.lparams(wrapContent, wrapContent) {
                    below(R.id.recently_play_title)
                    alignStart(R.id.recently_play_title)
                    setMargins(dip(5), dip(3), 0, dip(4))
                }
            }.lparams(wrapContent, wrapContent) {
                centerVertically()
                alignParentLeft()
            }

            imageView {
                id = R.id.recently_play_setting
                setImageResource(R.mipmap.list_icn_more)
            }.lparams(wrapContent, wrapContent) {
                alignParentRight()
                centerVertically()
                setMargins(0, 0, dip(5),0)
            }
        }
    }
}