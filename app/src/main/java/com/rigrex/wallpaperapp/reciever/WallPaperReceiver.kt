package com.rigrex.wallpaperapp.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rigrex.wallpaperapp.helpUtility.WallPaperHelper

class WallPaperReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        WallPaperHelper.invoke(context)
    }
}