package com.rigrex.wallpaperapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rigrex.wallpaperapp.database.dataModels.DownloadModel
import com.rigrex.wallpaperapp.database.dataModels.DownloadStatus

@Dao
interface DownloadDao {

    @Query("UPDATE download_table SET download_image_id = :downloadId, download_image_pid = :downloadPid, download_image_image_url = :imageURl,download_image_download_status = :status, download_is_selected = :isSelected WHERE download_image_id == :downloadId")
    fun updateDownload(
        downloadId: String,
        downloadPid: String,
        imageURl: String,
        status: Int,
        isSelected: Boolean
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToDownloads(downloadModel: DownloadModel)

    @Query("DELETE FROM download_table WHERE download_image_id == :downloadId")
    fun deleteFromDownloads(downloadId: String)

    @Query("SELECT * FROM download_table WHERE download_image_download_status == ${DownloadStatus.DOWNLOADED}")
    fun getAllDownloaded(): List<DownloadModel>

    @Query("SELECT * FROM download_table")
    fun getAllDownloads(): List<DownloadModel>

    @Query("SELECT * FROM download_table WHERE (download_image_id==:downloadId) LIMIT 1")
    fun getDownload(downloadId: String): DownloadModel?

    @Query("DELETE FROM download_table WHERE download_image_download_status != ${DownloadStatus.DOWNLOADED}")
    fun flushTable()
}