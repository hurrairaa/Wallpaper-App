package com.shehzad.wallpaperapp.unsplash

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Links_ {
    @SerializedName("self")
    @Expose
    var self: String? = null

    @SerializedName("html")
    @Expose
    var html: String? = null

    @SerializedName("photos")
    @Expose
    var photos: String? = null

    @SerializedName("likes")
    @Expose
    var likes: String? = null

    @SerializedName("portfolio")
    @Expose
    var portfolio: String? = null

    @SerializedName("following")
    @Expose
    var following: String? = null

    @SerializedName("followers")
    @Expose
    var followers: String? = null

}