package com.shehzad.wallpaperapp.pixabey

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Hit() : Parcelable {
    @SerializedName("largeImageURL")
    @Expose
    var largeImageURL: String? = null

    @SerializedName("webformatHeight")
    @Expose
    var webformatHeight: Int? = null

    @SerializedName("webformatWidth")
    @Expose
    var webformatWidth: Int? = null

    @SerializedName("likes")
    @Expose
    var likes: Int? = null

    @SerializedName("imageWidth")
    @Expose
    var imageWidth: Int? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

    @SerializedName("views")
    @Expose
    var views: Int? = null

    @SerializedName("comments")
    @Expose
    var comments: Int? = null

    @SerializedName("pageURL")
    @Expose
    var pageURL: String? = null

    @SerializedName("imageHeight")
    @Expose
    var imageHeight: Int? = null

    @SerializedName("webformatURL")
    @Expose
    var webformatURL: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("previewHeight")
    @Expose
    var previewHeight: Int? = null

    @SerializedName("tags")
    @Expose
    var tags: String? = null

    @SerializedName("downloads")
    @Expose
    var downloads: Int? = null

    @SerializedName("user")
    @Expose
    var user: String? = null

    @SerializedName("favorites")
    @Expose
    var favorites: Int? = null

    @SerializedName("imageSize")
    @Expose
    var imageSize: Int? = null

    @SerializedName("previewWidth")
    @Expose
    var previewWidth: Int? = null

    @SerializedName("userImageURL")
    @Expose
    var userImageURL: String? = null

    @SerializedName("previewURL")
    @Expose
    var previewURL: String? = null

    constructor(parcel: Parcel) : this() {
        largeImageURL = parcel.readString()
        webformatHeight = parcel.readValue(Int::class.java.classLoader) as? Int
        webformatWidth = parcel.readValue(Int::class.java.classLoader) as? Int
        likes = parcel.readValue(Int::class.java.classLoader) as? Int
        imageWidth = parcel.readValue(Int::class.java.classLoader) as? Int
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        userId = parcel.readValue(Int::class.java.classLoader) as? Int
        views = parcel.readValue(Int::class.java.classLoader) as? Int
        comments = parcel.readValue(Int::class.java.classLoader) as? Int
        pageURL = parcel.readString()
        imageHeight = parcel.readValue(Int::class.java.classLoader) as? Int
        webformatURL = parcel.readString()
        type = parcel.readString()
        previewHeight = parcel.readValue(Int::class.java.classLoader) as? Int
        tags = parcel.readString()
        downloads = parcel.readValue(Int::class.java.classLoader) as? Int
        user = parcel.readString()
        favorites = parcel.readValue(Int::class.java.classLoader) as? Int
        imageSize = parcel.readValue(Int::class.java.classLoader) as? Int
        previewWidth = parcel.readValue(Int::class.java.classLoader) as? Int
        userImageURL = parcel.readString()
        previewURL = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(largeImageURL)
        parcel.writeValue(webformatHeight)
        parcel.writeValue(webformatWidth)
        parcel.writeValue(likes)
        parcel.writeValue(imageWidth)
        parcel.writeValue(id)
        parcel.writeValue(userId)
        parcel.writeValue(views)
        parcel.writeValue(comments)
        parcel.writeString(pageURL)
        parcel.writeValue(imageHeight)
        parcel.writeString(webformatURL)
        parcel.writeString(type)
        parcel.writeValue(previewHeight)
        parcel.writeString(tags)
        parcel.writeValue(downloads)
        parcel.writeString(user)
        parcel.writeValue(favorites)
        parcel.writeValue(imageSize)
        parcel.writeValue(previewWidth)
        parcel.writeString(userImageURL)
        parcel.writeString(previewURL)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Hit> {
        override fun createFromParcel(parcel: Parcel): Hit {
            return Hit(parcel)
        }

        override fun newArray(size: Int): Array<Hit?> {
            return arrayOfNulls(size)
        }
    }
}