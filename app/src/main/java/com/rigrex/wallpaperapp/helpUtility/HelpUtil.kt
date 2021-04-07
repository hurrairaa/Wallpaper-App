package com.rigrex.wallpaperapp.helpUtility

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Toast
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.activities.DownloadInfoActivity
import com.rigrex.wallpaperapp.database.RoomRepository
import com.rigrex.wallpaperapp.database.dataModels.DownloadModel
import com.rigrex.wallpaperapp.database.dataModels.DownloadStatus
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import com.rigrex.wallpaperapp.databinding.WallpaperWindowBinding
import com.rigrex.wallpaperapp.helpUtility.transactions.FragmentTransactionHelper
import com.rigrex.wallpaperapp.network.DownloadUtil
import com.rigrex.wallpaperapp.network.helpUtil.NetworkHelpNew
import com.rigrex.wallpaperapp.uiComponents.ViewWatcher
import com.rigrex.wallpaperapp.uiComponents.WallPaperWindow


class HelpUtil {
    companion object {

        private var viewWatcher: ViewWatcher<WallPaperModel>? = null
        private var extensions: List<ViewWatcher.ViewWatcherExtension> = listOf()

        fun share(context: Context, link: String) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, link)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }

        fun getDefaultWindows(context: Context, layoutInflater: LayoutInflater): List<WallPaperWindow<WallPaperModel>> {
            return listOf(
                    WallPaperWindow<WallPaperModel>(WallpaperWindowBinding.inflate(layoutInflater), R.drawable.share_icon, "Share\nWallPaper").apply {
                        onSelected {
                            share(context, it.imageReferer ?: "")
                        }
                    },
                    WallPaperWindow<WallPaperModel>(WallpaperWindowBinding.inflate(layoutInflater), R.drawable.download, "Download\nWallPaper").apply {
                        onShow { getDownloadIcon(it.id, it.getWallPaperName()) { this.changeIcon(it) } }
                        onSelected { wallPaper ->
                            RoomRepository.getDownloading(wallPaper.id) {
                                if (it == null || it.downloadStatus == DownloadStatus.NORMAL) {
                                    DownloadUtil.download(DownloadModel().apply {
                                        id = wallPaper.id
                                        imageUrl = wallPaper.largeImageUrl
                                    }, null)
                                    runOnUiThread { Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show() }
                                } else {
                                    if (it.isDownloading()) {
                                        DownloadUtil.cancelDOwnload(it.pId?.toInt()!!)
                                        runOnUiThread { Toast.makeText(context, "Canceled...", Toast.LENGTH_SHORT).show() }
                                    } else {
                                        context.startActivity(Intent(context, DownloadInfoActivity::class.java).putExtra(DownloadInfoActivity.DOWNLOAD_MODEL, it))
                                    }
                                }
                                getDownloadIcon(wallPaper.id, wallPaper.getWallPaperName()) { this.changeIcon(it) }
                            }
                        }
                    },
                    WallPaperWindow<WallPaperModel>(WallpaperWindowBinding.inflate(layoutInflater), R.drawable.favorite_icon, "Add to\nFavorites").apply {
                        onShow { getFavIcon(it.id) { this.changeIcon(it) } }
                        onSelected {
                            RoomRepository.addOrDeleteFavorite(it.castToFav())
                            getFavIcon(it.id) { this.changeIcon(it) }
                        }
                    }
            )
        }

        fun startTimer() {
            startTimer(Preferences.getTime())
        }

        fun cancelTimer() {
            WallPaperHelper.removeTimer()
        }

        fun startTimer(time: String) {
            Preferences.setEnabled(true)
            WallPaperHelper.startTimer(time)
        }

        fun reloadAllData(context: Context) {
            synchronized(context) {
                NetworkHelpNew.flushData()
                RoomRepository.flushData()
                FragmentTransactionHelper.getVisibleFragment(context)?.also {
                    val temp = FragmentTransactionHelper.defaultFactory.provideFragment(it)
                    if (temp is FLushData) {
                        (temp as FLushData).flushData()
                    }
                }
            }
        }

        fun backupExtensions(vararg extensions: ViewWatcher.ViewWatcherExtension) {
            this.extensions = extensions.toList()
        }

        fun getExtensions(): List<ViewWatcher.ViewWatcherExtension> {
            return extensions
        }

        fun initializeWatcher(viewWatcher: ViewWatcher<WallPaperModel>) {
            this.viewWatcher = viewWatcher
        }

        fun getInstance(): ViewWatcher<WallPaperModel> {
            return viewWatcher ?: throw Exception("Please initialise the instance first")
        }

        fun restartApp(context: Context) {
            val packageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(context.packageName)
            val componentName = intent?.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            context.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }

        private fun runOnUiThread(handler: () -> Unit) {
            Handler(Looper.getMainLooper()).post(handler)
        }

        fun getFavIcon(id: String, icon: (Int) -> Unit) {
            RoomRepository.checkIfFav(id) {
                icon(if (it) R.drawable.heart_full else R.drawable.favorite_icon)
            }
        }

        fun getDownloadIcon(id: String, name: String, icon: (Int) -> Unit) {
            RoomRepository.isDownloaded(id) { isDownloaded ->
                if (isDownloaded) {
                    if (DownloadUtil.isDownloaded(name)) {
                        icon(R.drawable.downloaded)
                    } else {
                        RoomRepository.deleteDownloading(id)
                        icon(R.drawable.download)
                    }
                } else {
                    RoomRepository.isDownloading(id) { isDownloading ->
                        icon(if (isDownloading) R.drawable.cancel_download else R.drawable.download)
                    }
                }
            }
        }
    }

    interface FLushData {
        fun flushData()
    }
}