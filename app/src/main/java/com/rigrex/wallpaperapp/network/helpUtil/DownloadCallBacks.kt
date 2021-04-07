package com.rigrex.wallpaperapp.network.helpUtil

import com.downloader.Progress

interface DownloadCallBacks {
    fun onCancel()
    fun onProgress(progress: Int)
    fun onPauseDC()
    fun onStartResume()
    fun onComplete()
    fun onError(errorMessage: String)
}