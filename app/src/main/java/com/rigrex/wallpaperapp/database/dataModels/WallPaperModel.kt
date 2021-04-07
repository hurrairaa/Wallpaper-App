package com.rigrex.wallpaperapp.database.dataModels

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_wallpapers")
class WallPaperModel() : Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "image_id")
    var id: String = ""

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

    @ColumnInfo(name = "image_desc_avl")
    var descriptionAvl: Boolean = false

    @ColumnInfo(name = "image_referer")
    var imageReferer: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        previewImageUrl = parcel.readString()
        largeImageUrl = parcel.readString()
        imageCategory = parcel.readString()
        imageDimensions = parcel.readString()
        imageDownloads = parcel.readString()
        imageFileSize = parcel.readString()
        imageTags = parcel.readString()
        descriptionAvl = parcel.readByte() != 0.toByte()
        imageReferer = parcel.readString()
    }

    fun castToFav(): Favorite {
        return Favorite().apply {
            id = this@WallPaperModel.id
            previewImageUrl = this@WallPaperModel.previewImageUrl
            largeImageUrl = this@WallPaperModel.largeImageUrl
            imageCategory = this@WallPaperModel.imageCategory
            imageDimensions = this@WallPaperModel.imageDimensions
            imageDownloads = this@WallPaperModel.imageDownloads
            imageFileSize = this@WallPaperModel.imageFileSize
            imageTags = this@WallPaperModel.imageTags
        }
    }

    fun getTags(): List<String> {
        return imageTags?.split("|") ?: emptyList()
    }

    fun getWallPaperName(): String {
        return "${id}.jpg"
    }

    override fun describeContents(): Int {
        return 0
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
        parcel.writeByte(if (descriptionAvl) 1 else 0)
        parcel.writeString(imageReferer)
    }

    override fun toString(): String {
        return "WallPaperModel(id='$id', previewImageUrl=$previewImageUrl, largeImageUrl=$largeImageUrl, imageCategory=$imageCategory, imageDimensions=$imageDimensions, imageDownloads=$imageDownloads, imageFileSize=$imageFileSize, imageTags=$imageTags, descriptionAvl=$descriptionAvl, imageReferer=$imageReferer)"
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