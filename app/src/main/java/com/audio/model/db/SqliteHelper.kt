package com.audio.model.db

import com.audio.AudioApp
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper

abstract class SqliteHelper : ManagedSQLiteOpenHelper {

    constructor(name : String = "audio.db",version : Int) : super(AudioApp.instance, name, null, version)
}

