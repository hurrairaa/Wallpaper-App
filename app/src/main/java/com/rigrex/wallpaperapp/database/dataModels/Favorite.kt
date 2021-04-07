package com.rigrex.wallpaperapp.database.dataModels

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallpapers_favorite")
class Favorite() : Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "image_id")
    var id: String = ""

    @ColumnInfo(name = "category")
    var category: String? = null

    @ColumnInfo(name = "preview_image_url")
    var previewImageUrl: String? = null

    @ColumnInfo(name = "large_Image_url")
    var largeImageUrl: String? = null

    @ColumnInfo(name = "image_category")
    var imageCategory: String? = null

    @ColumnInfo(name = "image_dimensions")
    var imageDimensions: String? = null

    @ColumnInfo(name = "image_downloads")
    var imageDownloads: String? = null

    @ColumnInfo(name = "image_file_size")
    var imageFileSize: String? = null

    @ColumnInfo(name = "image_tags")
    var imageTags: String? = null

    @ColumnInfo(name = "image_referer")
    var imageReferer: String? = null

    @ColumnInfo(name = "image_desc_avl")
    var descriptionAvl = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        previewImageUrl = parcel.readString()
        largeImageUrl = parcel.readString()
        imageCategory = parcel.readString()
        imageDimensions = parcel.readString()
        imageDownloads = parcel.readString()
        imageFileSize = parcel.readString()
        imageTags = parcel.readString()
        imageReferer = parcel.readString()
        descriptionAvl = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(previewImageUrl)
        parcel.writeString(largeImageUrl)
        parcel.writeString(imageCategory)
        parcel.writeString(imageDimensions)
        parcel.writeString(imageDownloads)
        parcel.writeString(imageFileSize)
        parcel.writeString(imageTags)
        parcel.writeString(imageReferer)
        parcel.writeByte(if (descriptionAvl) 1 else 0)
    }

    fun castToModel(): WallPaperModel {
        return WallPaperModel().apply {
            id = this@Favorite.id
            previewImageUrl = this@Favorite.previewImageUrl
            largeImageUrl = this@Favorite.largeImageUrl
            imageCategory = this@Favorite.imageCategory
            imageDimensions = this@Favorite.imageDimensions
            imageDownloads = this@Favorite.imageDownloads
            imageFileSize = this@Favorite.imageFileSize
            imageTags = this@Favorite.imageTags
        }
    }

    fun getTags(): List<String> {
        return imageTags?.split("|") ?: emptyList()
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WallPaperModel> {
        override fun createFromParcel(parcel: Parcel): WallPaperModel {
            return WallPaperModel(parcel)
        }

        override fun newArray(size: Int): Array<WallPaperModel?> {
            return arrayOfNulls(size)
        }
    }
}