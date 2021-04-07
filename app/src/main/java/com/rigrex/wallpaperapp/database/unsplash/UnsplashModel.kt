package com.shehzad.wallpaperapp.unsplash

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UnsplashModel {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("width")
    @Expose
    var width: Int? = null

    @SerializedName("height")
    @Expose
    var height: Int? = null

    @SerializedName("color")
    @Expose
    var color: String? = null

    @SerializedName("description")
    @Expose
    var description: Any? = null

    @SerializedName("alt_description")
    @Expose
    var altDescription: String? = null

    @SerializedName("urls")
    @Expose
    var urls: Urls? = null

    @SerializedName("links")
    @Expose
    var links: Links? = null

    @SerializedName("categories")
    @Expose
    var categories: List<Any>? = null

    @SerializedName("sponsored")
    @Expose
    var sponsored: Boolean? = null

    @SerializedName("sponsored_by")
    @Expose
    var sponsoredBy: Any? = null

    @SerializedName("sponsored_impressions_id")
    @Expose
    var sponsoredImpressionsId: Any? = null

    @SerializedName("likes")
    @Expose
    var likes: Int? = null

    @SerializedName("liked_by_user")
    @Expose
    var likedByUser: Boolean? = null

    @SerializedName("current_user_collections")
    @Expose
    var currentUserCollections: List<Any>? = null

    @SerializedName("user")
    @Expose
    var user: User? = null

    @SerializedName("tags")
    @Expose
    var tags: List<Tag>? = null

}