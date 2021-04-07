package com.rigrex.wallpaperapp.uiComponents

import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewWatcher(var recyclerView: RecyclerView) : ViewWatcher.ViewWatcherExtension {
    override fun getHandler(): (Int, View) -> Unit {
        return { it, view ->
            if (it == ViewWatcher.START) {
                recyclerView.suppressLayout(true)
                recyclerView.children.forEach { if (view != it) it.isEnabled = false }
            } else if (it == ViewWatcher.END) {
                recyclerView.suppressLayout(false)
                recyclerView.children.forEach { it.isEnabled = true }
            }
        }
    }
}