package com.shehzad.wallpaperapp.unsplash

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UnsplashSearchModel {
    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null

    @SerializedName("results")
    @Expose
    var results: List<UnsplashModel>? = null

}