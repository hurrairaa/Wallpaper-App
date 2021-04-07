package com.shehzad.wallpaperapp.pixabey

class ResponseWrapper() {
    var largeImageUrl: String? = null
    var smallImageUrl: String? = null
    var imageID: String? = null
    var likes: String? = null
    var views: String? = null
    var tags: String? = null
    var description: String? = null
    var source: String? = null
    var webPageURL: String? = null
    var size: String? = null

    constructor(largeImageUrl: String?, smallImageUrl: String?, imageID: String?, likes: String?, views: String?, tags: String?, description: String?, source: String?, webPageURL: String?, size: String?) : this() {
        this.largeImageUrl = largeImageUrl
        this.smallImageUrl = smallImageUrl
        this.imageID = imageID
        this.likes = likes
        this.views = views
        this.tags = tags
        this.description = description
        this.source = source
        this.webPageURL = webPageURL
        this.size = size
    }

    fun isEmpty(): Boolean {
        return largeImageUrl?.isEmpty() ?: true && smallImageUrl?.isEmpty() ?: true
    }
}