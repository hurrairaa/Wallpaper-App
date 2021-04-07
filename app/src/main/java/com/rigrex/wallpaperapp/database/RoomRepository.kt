package com.rigrex.wallpaperapp.database

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.room.Room
import com.rigrex.wallpaperapp.database.dataModels.DownloadModel
import com.rigrex.wallpaperapp.database.dataModels.Favorite
import com.rigrex.wallpaperapp.database.dataModels.TimeModel
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class RoomRepository {
    companion object {
        private lateinit var dbInstance: AppDatabase
        fun initialise(context: Context) {
            if (!::dbInstance.isInitialized) {
                dbInstance =
                        Room.databaseBuilder(context, AppDatabase::class.java, "wallpaper-app-db")
                                .build()
                initiateDb()
            }
        }

        private fun initiateDb() {
            Schedulers.io().scheduleDirect {
                if (dbInstance.timeDao()?.getAllTimes().isNullOrEmpty()) {
                    dbInstance.timeDao()?.addTime(TimeModel().apply { name = "5 min" })
                    dbInstance.timeDao()?.addTime(TimeModel().apply { name = "10 min" })
                    dbInstance.timeDao()?.addTime(TimeModel().apply { name = "15 min" })
                    dbInstance.timeDao()?.addTime(TimeModel().apply { name = "20 min" })
                    dbInstance.timeDao()?.addTime(TimeModel().apply { name = "30 min" })
                    dbInstance.timeDao()?.addTime(TimeModel().apply { name = "40 min" })
                }
            }
        }

        fun flushData(handler: (() ->Unit)? = null) {
            Schedulers.io().scheduleDirect {
                dbInstance.offlineDao()?.flushTable()
                dbInstance.downloadDao()?.flushTable()
                handler?.invoke()
            }
        }

        fun getAllTimes(invoker: (List<TimeModel>) -> Unit) {
            Schedulers.io().scheduleDirect {
                invoker.invoke(dbInstance.timeDao()?.getAllTimes() ?: mutableListOf())
            }
        }

        fun addTime(timeModel: TimeModel) {
            Schedulers.io().scheduleDirect {
                dbInstance.timeDao()?.addTime(timeModel)
            }
        }

        fun addOrDeleteFavorite(favorite: Favorite) {
            checkIfFav(favorite.id) {
                if (it) {
                    deleteFavorite(favorite)
                } else {
                    addFavorite(favorite)
                }
            }
        }

        fun deleteFavorite(favorite: Favorite) {
            Schedulers.io().scheduleDirect {
                dbInstance.favoriteDao()?.deleteFromFav(favorite.id)
            }
        }

        fun addFavorite(favorite: Favorite) {
            Schedulers.io().scheduleDirect {
                dbInstance.favoriteDao()?.addToFav(favorite)
            }
        }

        fun checkIfFav(id: String, inform: (Boolean) -> Unit) {
            Schedulers.io()
                    .scheduleDirect { inform(dbInstance.favoriteDao()?.getFavorite(id) != null) }
        }

        fun addCatData(list: List<WallPaperModel>) {
            Schedulers.io().scheduleDirect {
                kotlin.runCatching { dbInstance.offlineDao()?.addToDb(list) }
            }
        }

        fun updateDownloadModels(list: List<DownloadModel>) {
            Schedulers.io().scheduleDirect {
                list.forEach { downloadModel ->
                    dbInstance.downloadDao()?.updateDownload(
                            downloadId = downloadModel.id,
                            downloadPid = downloadModel.pId ?: "",
                            imageURl = downloadModel.imageUrl ?: "",
                            status = downloadModel.downloadStatus,
                            isSelected = downloadModel.isSelected
                    )
                }
            }
        }

        fun getAllDownloadWallpapers(informer: (List<DownloadModel>) -> Unit) {
            Schedulers.io().scheduleDirect {
                informer(dbInstance.downloadDao()?.getAllDownloaded() ?: emptyList())
            }
        }

        fun updateWallPaperModel(wallPaperModel: WallPaperModel) {
            Schedulers.io().scheduleDirect {
                dbInstance.offlineDao()?.updateModel(
                        imageId = wallPaperModel.id,
                        largeImageUrl = wallPaperModel.largeImageUrl,
                        previewImageUrl = wallPaperModel.previewImageUrl,
                        imageCategory = wallPaperModel.imageCategory,
                        imageDimensions = wallPaperModel.imageDimensions,
                        imageDownloads = wallPaperModel.imageDownloads,
                        imageFileSize = wallPaperModel.imageFileSize,
                        imageTags = wallPaperModel.imageTags,
                        imageDescrAvl = wallPaperModel.descriptionAvl
                )
            }
        }

        fun getDownload(id: String, informer: (DownloadModel?) -> Unit) {
            Schedulers.io().scheduleDirect {
                informer(dbInstance.downloadDao()?.getDownload(id))
            }
        }

        fun deleteDownloading(id: String) {
            Schedulers.io().scheduleDirect { dbInstance.downloadDao()?.deleteFromDownloads(id) }
        }

        fun updateDownloading(downloadModel: DownloadModel) {
            Schedulers.io().scheduleDirect {
                dbInstance.downloadDao()?.updateDownload(
                        downloadId = downloadModel.id,
                        downloadPid = downloadModel.pId ?: "",
                        imageURl = downloadModel.imageUrl ?: "",
                        status = downloadModel.downloadStatus,
                        isSelected = downloadModel.isSelected
                )
            }
        }

        fun isDownloading(id: String, informer: (Boolean) -> Unit) {
            Schedulers.io().scheduleDirect {
                var download = dbInstance.downloadDao()?.getDownload(id)
                informer((download != null && download.isDownloading()))
            }
        }

        fun isDownloaded(id: String, informer: (Boolean) -> Unit) {
            Schedulers.io().scheduleDirect {
                var download = dbInstance.downloadDao()?.getDownload(id)
                informer((download != null && download.isDownloaded()))
            }
        }

        fun getDownloading(id: String, informer: (DownloadModel?) -> Unit) {
            Schedulers.io().scheduleDirect {
                informer(dbInstance.downloadDao()?.getDownload(id))
            }
        }

        fun addDownload(downloadModel: DownloadModel) {
            Schedulers.io().scheduleDirect {
                dbInstance.downloadDao()?.addToDownloads(downloadModel)
            }
        }

        fun getOfflineData(
                category: String,
                informer: (Observable<List<WallPaperModel>?>?) -> Unit
        ) {
            Schedulers.io().scheduleDirect {
                informer(
                        Observable
                                .just(dbInstance.offlineDao()?.getCatDataList(category))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                )
            }
        }

        fun getFavorites(lifecycle: LifecycleOwner, informer: (List<Favorite>) -> Unit) {
            dbInstance.favoriteDao()?.getAllFavorites()?.observe(lifecycle) { informer(it) }
        }
    }
}