package com.rigrex.wallpaperapp.uiComponents

import android.view.View

class ViewGroupExtension(var views: Array<View>) : ViewWatcher.ViewWatcherExtension {
    override fun getHandler(): (Int, View) -> Unit {
        return { it, _ ->
            if (it == ViewWatcher.START) {
                views.forEach { it.isEnabled = false }
            } else if (it == ViewWatcher.END) {
                views.forEach { it.isEnabled = true }
            }
        }
    }
}