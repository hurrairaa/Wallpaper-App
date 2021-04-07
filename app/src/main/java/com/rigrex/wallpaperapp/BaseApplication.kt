package com.rigrex.wallpaperapp

import android.app.Application
import com.rigrex.wallpaperapp.database.RoomRepository
import com.rigrex.wallpaperapp.helpUtility.Preferences
import com.rigrex.wallpaperapp.helpUtility.WallPaperHelper
import com.rigrex.wallpaperapp.network.DownloadUtil

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RoomRepository.initialise(this)
        DownloadUtil.init(this)
        WallPaperHelper.initialize(this)
        Preferences.initialize(this)
        RoomRepository.flushData()
    }
}