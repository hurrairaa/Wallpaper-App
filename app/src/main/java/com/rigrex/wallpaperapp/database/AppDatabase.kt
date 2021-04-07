package com.rigrex.wallpaperapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rigrex.wallpaperapp.database.dao.DownloadDao
import com.rigrex.wallpaperapp.database.dao.FavoriteDao
import com.rigrex.wallpaperapp.database.dao.OfflineDao
import com.rigrex.wallpaperapp.database.dao.TimeDao
import com.rigrex.wallpaperapp.database.dataModels.DownloadModel
import com.rigrex.wallpaperapp.database.dataModels.Favorite
import com.rigrex.wallpaperapp.database.dataModels.TimeModel
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel

@Database(
        entities = [Favorite::class, WallPaperModel::class, DownloadModel::class, TimeModel::class],
        version = 1,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao?
    abstract fun offlineDao(): OfflineDao?
    abstract fun downloadDao(): DownloadDao?
    abstract fun timeDao(): TimeDao?
}