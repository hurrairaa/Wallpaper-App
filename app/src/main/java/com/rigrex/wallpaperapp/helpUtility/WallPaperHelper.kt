package com.rigrex.wallpaperapp.helpUtility

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.rigrex.wallpaperapp.database.RoomRepository
import com.rigrex.wallpaperapp.database.dataModels.DownloadModel
import com.rigrex.wallpaperapp.database.dataModels.TimeModel
import com.rigrex.wallpaperapp.network.DownloadUtil
import com.rigrex.wallpaperapp.reciever.WallPaperReceiver
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.*


class WallPaperHelper {
    companion object {

        const val WALLPAPER_CHANGE_REQUEST = 987
        private const val TAG = "AutoWallpaperSetting"
        private var mgr: AlarmManager? = null
        private var pendingIntent: PendingIntent? = null

        fun initialize(context: Context) {
            pendingIntent = PendingIntent.getBroadcast(context, WALLPAPER_CHANGE_REQUEST, Intent(context, WallPaperReceiver::class.java), 0)
            mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }

        fun startTimer(timeModel: String) {
            removeTimer()
            mgr?.setExact(AlarmManager.RTC, decipherTime(timeModel), pendingIntent)
        }

        private fun decipherTime(value: String): Long {
            var time = Calendar.getInstance()
            time.timeInMillis = System.currentTimeMillis()
            if (value.endsWith("min")) {
                time.add(Calendar.MINUTE, value.split(" ")[0].toInt())
            } else {
                time.add(Calendar.HOUR, value.split(" ")[0].toInt())
            }
            return time.timeInMillis
        }

        private fun handleDBTask(context: Context, invoker: (List<DownloadModel>) -> Unit) {
            RoomRepository.initialise(context)
            RoomRepository.getAllDownloadWallpapers { invoker.invoke(it.filter { it.isSelected }) }
        }

        private fun adjustNextWal(context: Context, list: MutableList<DownloadModel>): DownloadModel? {
            Preferences.initialize(context)
            var id = Preferences.getCurrent()
            if (id == Preferences.DEFAULT_VALUES) {
                return if (list.isEmpty()) {
                    null
                } else {
                    var s = list[0].also { Preferences.setCurrent(it) }
                    return if (File("${DownloadUtil.DOWNLOAD_PATH}/${s.getWallPaperName()}").exists()) {
                        s
                    } else {
                        list.remove(s)
                        RoomRepository.deleteDownloading(s.id)
                        adjustNextWal(context, list)
                    }
                }
            } else {
                if (list.isEmpty()) {
                    return null
                } else {
                    var index = list.indexOfFirst { it.id == Preferences.getCurrent() }
                    if (index < 0) {
                        index = 0
                    } else {
                        ++index
                        if (index >= list.size) {
                            index = 0
                        }
                    }
                    var s = list[index].also { Preferences.setCurrent(it) }
                    return if (File("${DownloadUtil.DOWNLOAD_PATH}/${s.getWallPaperName()}").exists()) {
                        s
                    } else {
                        list.remove(s)
                        RoomRepository.deleteDownloading(s.id)
                        adjustNextWal(context, list)
                    }
                }
            }
        }

        fun invoke(context: Context) {
            initialize(context)
            if (!Preferences.isEnabled()) {
                removeTimer()
            } else {
                handleDBTask(context) {
                    adjustNextWal(context, it.toMutableList()).let {
                        if (it == null) {
                            removeTimer()
                        } else {
                            setWallPaper(it, context, WallpaperManager.FLAG_SYSTEM)
                            startTimer(Preferences.getTime())
                        }
                    }
                }
            }
        }

        fun removeTimer() {
            mgr?.cancel(pendingIntent)
        }

        fun setWallPaper(downloadModel: DownloadModel, context: Context, which: Int) {
            runBlocking {
                try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        Log.d("WallPaperHelper", "setWallPaper: this is start")
                        WallpaperManager
                                .getInstance(context)
                                .apply {
                                    setWallpaperOffsetSteps(1f, 1f)
                                }.setBitmap(
                                        BitmapFactory
                                                .decodeFile("${DownloadUtil.DOWNLOAD_PATH}${downloadModel.getWallPaperName()}"),
                                        null,
                                        true,
                                        which
                                )
                    } else {
                        WallpaperManager
                                .getInstance(context)
                                .apply {
                                    setWallpaperOffsetSteps(1f, 1f)
                                    context.startActivity(
                                            getCropAndSetWallpaperIntent(Uri.fromFile(File("${DownloadUtil.DOWNLOAD_PATH}${downloadModel.getWallPaperName()}")))
                                    )
                                }
                    }
                } catch (e: Exception) {
                    Log.d("WallPaperHelper", "setWallPaper: error occurred while downloading")
                }
            }
        }

        fun createWalPaperChooser(context: Context, invoker: (Int) -> Unit) {
            createChooseR(
                    "Select As",
                    listOf("Home and Lock Screen", "HomeScreen Only", "LockScreen Only"),
                    {
                        Log.d("WallPaperHelper", "createWalPaperChooser: $it")
                        when (it) {
                            "HomeScreen Only" -> {
                                invoker(WallpaperManager.FLAG_SYSTEM)
                            }
                            "LockScreen Only" -> {
                                invoker(WallpaperManager.FLAG_LOCK)
                            }
                            "Home and Lock Screen" -> {
                                invoker(WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
                            }
                        }
                    },
                    context
            )
        }

        fun createChooseR(
                title: String,
                actions: List<String>,
                invoker: (String) -> Unit,
                context: Context
        ) {
            AlertDialog
                    .Builder(context)
                    .setTitle(title)
                    .setItems(actions.toTypedArray()) { _, which ->
                        Log.d("WallPaperHelper", "createChooseR: ${actions[which]}")
                        invoker(actions[which])
                    }
                    .show()
        }
    }
}