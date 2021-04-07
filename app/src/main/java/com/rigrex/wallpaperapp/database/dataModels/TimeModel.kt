package com.rigrex.wallpaperapp.database.dataModels

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auto_time")
class TimeModel() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "t_id")
    var id: Int = 0

    @ColumnInfo(name = "title")
    var name: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString() ?: ""
    }


    override fun toString(): String {
        return "TimeModel(id=$id, name='$name')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TimeModel> {
        override fun createFromParcel(parcel: Parcel): TimeModel {
            return TimeModel(parcel)
        }

        override fun newArray(size: Int): Array<TimeModel?> {
            return arrayOfNulls(size)
        }
    }
}