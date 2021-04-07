package com.rigrex.wallpaperapp.database.dataModels

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_table")
class DownloadModel() : Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "download_image_id")
    var id: String = ""

    @ColumnInfo(name = "download_image_pid")
    var pId: String? = null

    @ColumnInfo(name = "download_image_image_url")
    var imageUrl: String? = null

    @ColumnInfo(name = "download_image_download_status")
    var downloadStatus: Int = DownloadStatus.NORMAL

    @ColumnInfo(name = "download_is_selected")
    var isSelected = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        pId = parcel.readString()
        imageUrl = parcel.readString()
    }

    fun isDownloading() = downloadStatus == DownloadStatus.DOWNLOADING
    fun isDownloaded() = downloadStatus == DownloadStatus.DOWNLOADED

    fun getWallPaperName(): String {
        return "${id}.jpg"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(pId)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        return if (other is DownloadModel?){
            other?.id == id
        }else{
            false
        }
    }

    override fun toString(): String {
        return "DownloadModel(id='$id', pId=$pId, imageUrl=$imageUrl, downloadStatus=$downloadStatus)"
    }

    companion object CREATOR : Parcelable.Creator<DownloadModel> {
        override fun createFromParcel(parcel: Parcel): DownloadModel {
            return DownloadModel(parcel)
        }

        override fun newArray(size: Int): Array<DownloadModel?> {
            return arrayOfNulls(size)
        }
    }
}

class DownloadStatus {
    companion object {
        const val DOWNLOADING = 0
        const val DOWNLOADED = 1
        const val NORMAL = 2
    }
}