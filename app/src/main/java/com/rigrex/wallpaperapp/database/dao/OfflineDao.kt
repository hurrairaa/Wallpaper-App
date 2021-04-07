package com.rigrex.wallpaperapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel

@Dao
interface OfflineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToDb(list: List<WallPaperModel>)

    @Query("DELETE FROM offline_wallpapers")
    fun flushTable()

    @Query("SELECT * FROM offline_wallpapers WHERE image_category == :category")
    fun getCatData(category: String): LiveData<List<WallPaperModel>>

    @Query("UPDATE offline_wallpapers SET" +
            " large_Image_url = :largeImageUrl," +
            " preview_image_url = :previewImageUrl," +
            " image_category = :imageCategory," +
            " image_dimensions = :imageDimensions," +
            " image_downloads =:imageDownloads," +
            " image_file_size = :imageFileSize," +
            " image_tags = :imageTags," +
            " image_desc_avl = :imageDescrAvl" +
            " WHERE image_id = :imageId"
    )
    fun updateModel(imageId: String?,
                    largeImageUrl: String?,
                    previewImageUrl: String?,
                    imageCategory: String?,
                    imageDimensions: String?,
                    imageDownloads: String?,
                    imageFileSize: String?,
                    imageTags: String?,
                    imageDescrAvl: Boolean?)

    @Query("SELECT * FROM offline_wallpapers WHERE image_category == :category")
    fun getCatDataList(category: String): List<WallPaperModel>

    @Query("DELETE FROM offline_wallpapers WHERE image_category == :category")
    fun deleteCat(category: String)

    @Query("SELECT * FROM offline_wallpapers WHERE (image_category == :category) LIMIT :limit OFFSET :offset")
    fun getCatDataPaginated(category: String, limit: String, offset: String): List<WallPaperModel>
}