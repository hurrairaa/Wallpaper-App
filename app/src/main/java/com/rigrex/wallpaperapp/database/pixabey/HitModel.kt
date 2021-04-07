package com.shehzad.wallpaperapp.pixabey

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HitModel() : Parcelable {
    @SerializedName("totalHits")
    @Expose
    var totalHits: Int? = null

    @SerializedName("hits")
    @Expose
    var hits: List<Hit>? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null

    constructor(parcel: Parcel) : this() {
        totalHits = parcel.readValue(Int::class.java.classLoader) as? Int
        hits = parcel.createTypedArrayList(Hit)
        total = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(totalHits)
        parcel.writeTypedList(hits)
        parcel.writeValue(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HitModel> {
        override fun createFromParcel(parcel: Parcel): HitModel {
            return HitModel(parcel)
        }

        override fun newArray(size: Int): Array<HitModel?> {
            return arrayOfNulls(size)
        }
    }
}