package com.rigrex.wallpaperapp.network

import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.View
import com.downloader.*
import com.google.android.material.snackbar.Snackbar
import com.rigrex.wallpaperapp.database.RoomRepository
import com.rigrex.wallpaperapp.database.dataModels.DownloadModel
import com.rigrex.wallpaperapp.database.dataModels.DownloadStatus
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import com.rigrex.wallpaperapp.network.helpUtil.DownloadCallBacks
import java.io.File
import java.math.RoundingMode
import java.net.URL

class DownloadUtil {
    companion object {
        val DOWNLOAD_PATH = "${Environment.getExternalStorageDirectory()}/WalRex/"
        private var downloading = hashMapOf<DownloadModel, DownloadCallBacks?>().toMutableMap()
        private var queueCalls = hashMapOf<String, (DownloadModel) -> Unit>().toMutableMap()
        private lateinit var context: Context

        fun init(context: Context) {
            Companion.context = context
            File(DOWNLOAD_PATH).takeIf { !it.exists() }?.let { it.mkdirs();it.mkdir(); }
            val config = PRDownloaderConfig
                    .newBuilder()
                    .setDatabaseEnabled(true)
                    .build()
            PRDownloader.initialize(context, config)
        }

        private fun fileCleanUp(name: String) {
            File("$DOWNLOAD_PATH${name}").deleteOnExit()
        }

        fun isDownloaded(name: String) =
                File("$DOWNLOAD_PATH$name").exists()

        fun resumeDownload(pId: Int) {
            PRDownloader.resume(pId)
        }

        fun pauseDownloading(pId: Int) {
            PRDownloader.pause(pId)
        }

        fun cancelDOwnload(pId: Int) {
            PRDownloader.cancel(pId)
        }

        fun cancelAll() {
            PRDownloader.cancelAll()
        }

        fun cancelSelected(list: List<DownloadModel>) {
            list.forEach {
                cancelDOwnload(it.pId?.toInt() ?: -1)
            }
        }

        private fun performAction(action: (DownloadModel) -> Unit) {
            downloading.keys.forEach {
                action(it)
            }
        }

        fun pauseAll() {
            performAction {
                PRDownloader.pause(it.pId?.toInt() ?: -1)
            }
        }

        fun resumeAll() {
            performAction {
                PRDownloader.resume(it.pId?.toInt() ?: -1)
            }
        }

        fun registerListener(id: String, downloadCall: DownloadCallBacks) {
            synchronized(this) {
                findListener(id)?.let {
                    downloading[it] = downloadCall
                }
            }
        }

        private fun findListener(id: String): DownloadModel? {
            synchronized(this) {
                downloading.keys.indexOfFirst { it.id == id }.also {
                    return if (it >= 0) {
                        downloading.keys.toMutableList()[it]
                    } else {
                        null
                    }
                }
            }
        }

        private fun getRequiredListener(id: String): DownloadCallBacks? {
            findListener(id).also {
                return if (it == null)
                    null
                else
                    downloading[it]
            }
        }

        fun unRegisterListener(id: String) {
            synchronized(this) {
                findListener(id)?.let { downloading[it] = null }
            }
        }

        fun removeListener(id: String) {
            synchronized(this) {
                findListener(id)?.let { downloading.remove(it) }
            }
        }

        private fun addListener(downloadModel: DownloadModel, downloadCall: DownloadCallBacks) {
            downloading[downloadModel] = downloadCall
        }

        fun attachQueueCalls(id: String, invoker: (DownloadModel) -> Unit) {
            queueCalls[id] = invoker
        }

        fun download(
                downloadModel: DownloadModel,
                downloadCall: DownloadCallBacks?
        ) {
            if (downloadCall != null)
                addListener(downloadModel, downloadCall)
            var pId = PRDownloader
                    .download(
                            downloadModel.imageUrl,
                            DOWNLOAD_PATH,
                            downloadModel.getWallPaperName()
                    )
                    .build()
                    .setOnCancelListener {
                        fileCleanUp(downloadModel.getWallPaperName())
                        downloadModel.downloadStatus = DownloadStatus.NORMAL
                        RoomRepository.updateDownloading(downloadModel)
                        getRequiredListener(downloadModel.id)?.onCancel()
                        removeListener(id = downloadModel.id)
                    }
                    .setOnProgressListener {
                        getRequiredListener(downloadModel.id ?: "")
                                ?.onProgress(((it.currentBytes.toFloat() / it.totalBytes.toFloat()) * 100f).toInt())
                    }
                    .setOnPauseListener {
                        RoomRepository.updateDownloading(downloadModel)
                        getRequiredListener(downloadModel.id ?: "")?.onPauseDC()
                    }
                    .setOnStartOrResumeListener {
                        downloadModel.downloadStatus = DownloadStatus.DOWNLOADING
                        RoomRepository.addDownload(downloadModel)
                        getRequiredListener(downloadModel.id ?: "")?.onStartResume()
                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            getRequiredListener(downloadModel.id)?.onComplete()
                            downloadModel.downloadStatus = DownloadStatus.DOWNLOADED
                            Log.d("WallPaperHelper", "downloaded: $downloadModel")
                            RoomRepository.updateDownloading(downloadModel)
                            removeListener(id = downloadModel.id)
                            queueCalls[downloadModel.id]?.let {
                                it.invoke(downloadModel)
                                queueCalls.remove(downloadModel.id)
                            }
                        }

                        override fun onError(error: Error?) {
                            downloadModel.downloadStatus = DownloadStatus.NORMAL
                            RoomRepository.updateDownloading(downloadModel)
                            fileCleanUp(downloadModel.getWallPaperName())
                            getRequiredListener(downloadModel.id ?: "")?.onError("$error")
                        }
                    })
            downloadModel.apply { this.pId = "$pId" }
            Log.d("WallPaperHelper", "download: $downloadModel")
        }
    }
}