package com.rigrex.wallpaperapp.database.unsplash.unsplashDescriptionData

import com.google.gson.annotations.SerializedName

data class UnSplashModel(
        @SerializedName("alt_description")
        var altDescription: String?,
        @SerializedName("blur_hash")
        var blurHash: String?,
        @SerializedName("categories")
        var categories: List<Any>?,
        @SerializedName("color")
        var color: String?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("current_user_collections")
        var currentUserCollections: List<Any>?,
        @SerializedName("description")
        var description: String?,
        @SerializedName("downloads")
        var downloads: Int?,
        @SerializedName("exif")
        var exif: Exif?,
        @SerializedName("height")
        var height: Int?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("liked_by_user")
        var likedByUser: Boolean?,
        @SerializedName("likes")
        var likes: Int?,
        @SerializedName("links")
        var links: Links?,
        @SerializedName("location")
        var location: Location?,
        @SerializedName("meta")
        var meta: Meta?,
        @SerializedName("promoted_at")
        var promotedAt: Any?,
        @SerializedName("related_collections")
        var relatedCollections: RelatedCollections?,
        @SerializedName("sponsorship")
        var sponsorship: Sponsorship?,
        @SerializedName("tags")
        var tags: List<TagX>?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("urls")
        var urls: UrlsXXXX?,
        @SerializedName("user")
        var user: UserXXXX?,
        @SerializedName("views")
        var views: Int?,
        @SerializedName("width")
        var width: Int?
)

data class Exif(
        @SerializedName("aperture")
        var aperture: String?,
        @SerializedName("exposure_time")
        var exposureTime: String?,
        @SerializedName("focal_length")
        var focalLength: String?,
        @SerializedName("iso")
        var iso: Int?,
        @SerializedName("make")
        var make: String?,
        @SerializedName("model")
        var model: String?
)

data class Links(
        @SerializedName("download")
        var download: String?,
        @SerializedName("download_location")
        var downloadLocation: String?,
        @SerializedName("html")
        var html: String?,
        @SerializedName("self")
        var self: String?
)

data class Location(
        @SerializedName("city")
        var city: Any?,
        @SerializedName("country")
        var country: Any?,
        @SerializedName("name")
        var name: Any?,
        @SerializedName("position")
        var position: Position?,
        @SerializedName("title")
        var title: Any?
)

data class Meta(
        @SerializedName("index")
        var index: Boolean?
)

data class RelatedCollections(
        @SerializedName("results")
        var results: List<Result>?,
        @SerializedName("total")
        var total: Int?,
        @SerializedName("type")
        var type: String?
)

data class Sponsorship(
        @SerializedName("impression_urls")
        var impressionUrls: List<String>?,
        @SerializedName("sponsor")
        var sponsor: Sponsor?,
        @SerializedName("tagline")
        var tagline: String?,
        @SerializedName("tagline_url")
        var taglineUrl: String?
)

data class TagX(
        @SerializedName("source")
        var source: SourceX?,
        @SerializedName("title")
        var title: String?,
        @SerializedName("type")
        var type: String?
)

data class UrlsXXXX(
        @SerializedName("full")
        var full: String?,
        @SerializedName("raw")
        var raw: String?,
        @SerializedName("regular")
        var regular: String?,
        @SerializedName("small")
        var small: String?,
        @SerializedName("thumb")
        var thumb: String?
)

data class UserXXXX(
        @SerializedName("accepted_tos")
        var acceptedTos: Boolean?,
        @SerializedName("bio")
        var bio: String?,
        @SerializedName("first_name")
        var firstName: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("instagram_username")
        var instagramUsername: String?,
        @SerializedName("last_name")
        var lastName: Any?,
        @SerializedName("links")
        var links: LinksXXXXXXXXXX?,
        @SerializedName("location")
        var location: Any?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("portfolio_url")
        var portfolioUrl: String?,
        @SerializedName("profile_image")
        var profileImage: ProfileImageXXXXX?,
        @SerializedName("total_collections")
        var totalCollections: Int?,
        @SerializedName("total_likes")
        var totalLikes: Int?,
        @SerializedName("total_photos")
        var totalPhotos: Int?,
        @SerializedName("twitter_username")
        var twitterUsername: String?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("username")
        var username: String?
)

data class Position(
        @SerializedName("latitude")
        var latitude: Any?,
        @SerializedName("longitude")
        var longitude: Any?
)

data class Result(
        @SerializedName("cover_photo")
        var coverPhoto: CoverPhoto?,
        @SerializedName("curated")
        var curated: Boolean?,
        @SerializedName("description")
        var description: Any?,
        @SerializedName("featured")
        var featured: Boolean?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("last_collected_at")
        var lastCollectedAt: String?,
        @SerializedName("links")
        var links: LinksXXX?,
        @SerializedName("preview_photos")
        var previewPhotos: List<PreviewPhoto>?,
        @SerializedName("private")
        var `private`: Boolean?,
        @SerializedName("published_at")
        var publishedAt: String?,
        @SerializedName("share_key")
        var shareKey: String?,
        @SerializedName("tags")
        var tags: List<Tag>?,
        @SerializedName("title")
        var title: String?,
        @SerializedName("total_photos")
        var totalPhotos: Int?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("user")
        var user: UserXX?
)

data class CoverPhoto(
        @SerializedName("alt_description")
        var altDescription: String?,
        @SerializedName("blur_hash")
        var blurHash: String?,
        @SerializedName("categories")
        var categories: List<Any>?,
        @SerializedName("color")
        var color: String?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("current_user_collections")
        var currentUserCollections: List<Any>?,
        @SerializedName("description")
        var description: String?,
        @SerializedName("height")
        var height: Int?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("liked_by_user")
        var likedByUser: Boolean?,
        @SerializedName("likes")
        var likes: Int?,
        @SerializedName("links")
        var links: LinksX?,
        @SerializedName("promoted_at")
        var promotedAt: Any?,
        @SerializedName("sponsorship")
        var sponsorship: Any?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("urls")
        var urls: Urls?,
        @SerializedName("user")
        var user: User?,
        @SerializedName("width")
        var width: Int?
)

data class LinksXXX(
        @SerializedName("html")
        var html: String?,
        @SerializedName("photos")
        var photos: String?,
        @SerializedName("related")
        var related: String?,
        @SerializedName("self")
        var self: String?
)

data class PreviewPhoto(
        @SerializedName("blur_hash")
        var blurHash: String?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("urls")
        var urls: UrlsX?
)

data class Tag(
        @SerializedName("source")
        var source: Source?,
        @SerializedName("title")
        var title: String?,
        @SerializedName("type")
        var type: String?
)

data class UserXX(
        @SerializedName("accepted_tos")
        var acceptedTos: Boolean?,
        @SerializedName("bio")
        var bio: Any?,
        @SerializedName("first_name")
        var firstName: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("instagram_username")
        var instagramUsername: Any?,
        @SerializedName("last_name")
        var lastName: String?,
        @SerializedName("links")
        var links: LinksXXXXXX?,
        @SerializedName("location")
        var location: Any?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("portfolio_url")
        var portfolioUrl: Any?,
        @SerializedName("profile_image")
        var profileImage: ProfileImageXX?,
        @SerializedName("total_collections")
        var totalCollections: Int?,
        @SerializedName("total_likes")
        var totalLikes: Int?,
        @SerializedName("total_photos")
        var totalPhotos: Int?,
        @SerializedName("twitter_username")
        var twitterUsername: Any?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("username")
        var username: String?
)

data class LinksX(
        @SerializedName("download")
        var download: String?,
        @SerializedName("download_location")
        var downloadLocation: String?,
        @SerializedName("html")
        var html: String?,
        @SerializedName("self")
        var self: String?
)

data class Urls(
        @SerializedName("full")
        var full: String?,
        @SerializedName("raw")
        var raw: String?,
        @SerializedName("regular")
        var regular: String?,
        @SerializedName("small")
        var small: String?,
        @SerializedName("thumb")
        var thumb: String?
)

data class User(
        @SerializedName("accepted_tos")
        var acceptedTos: Boolean?,
        @SerializedName("bio")
        var bio: String?,
        @SerializedName("first_name")
        var firstName: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("instagram_username")
        var instagramUsername: String?,
        @SerializedName("last_name")
        var lastName: String?,
        @SerializedName("links")
        var links: LinksXX?,
        @SerializedName("location")
        var location: String?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("portfolio_url")
        var portfolioUrl: String?,
        @SerializedName("profile_image")
        var profileImage: ProfileImage?,
        @SerializedName("total_collections")
        var totalCollections: Int?,
        @SerializedName("total_likes")
        var totalLikes: Int?,
        @SerializedName("total_photos")
        var totalPhotos: Int?,
        @SerializedName("twitter_username")
        var twitterUsername: Any?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("username")
        var username: String?
)

data class LinksXX(
        @SerializedName("followers")
        var followers: String?,
        @SerializedName("following")
        var following: String?,
        @SerializedName("html")
        var html: String?,
        @SerializedName("likes")
        var likes: String?,
        @SerializedName("photos")
        var photos: String?,
        @SerializedName("portfolio")
        var portfolio: String?,
        @SerializedName("self")
        var self: String?
)

data class ProfileImage(
        @SerializedName("large")
        var large: String?,
        @SerializedName("medium")
        var medium: String?,
        @SerializedName("small")
        var small: String?
)

data class UrlsX(
        @SerializedName("full")
        var full: String?,
        @SerializedName("raw")
        var raw: String?,
        @SerializedName("regular")
        var regular: String?,
        @SerializedName("small")
        var small: String?,
        @SerializedName("thumb")
        var thumb: String?
)

data class Source(
        @SerializedName("ancestry")
        var ancestry: Ancestry?,
        @SerializedName("cover_photo")
        var coverPhoto: CoverPhotoX?,
        @SerializedName("description")
        var description: String?,
        @SerializedName("meta_description")
        var metaDescription: String?,
        @SerializedName("meta_title")
        var metaTitle: String?,
        @SerializedName("subtitle")
        var subtitle: String?,
        @SerializedName("title")
        var title: String?
)

data class Ancestry(
        @SerializedName("category")
        var category: Category?,
        @SerializedName("subcategory")
        var subcategory: Subcategory?,
        @SerializedName("type")
        var type: Type?
)

data class CoverPhotoX(
        @SerializedName("alt_description")
        var altDescription: String?,
        @SerializedName("blur_hash")
        var blurHash: String?,
        @SerializedName("categories")
        var categories: List<Any>?,
        @SerializedName("color")
        var color: String?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("current_user_collections")
        var currentUserCollections: List<Any>?,
        @SerializedName("description")
        var description: String?,
        @SerializedName("height")
        var height: Int?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("liked_by_user")
        var likedByUser: Boolean?,
        @SerializedName("likes")
        var likes: Int?,
        @SerializedName("links")
        var links: LinksXXXX?,
        @SerializedName("promoted_at")
        var promotedAt: String?,
        @SerializedName("sponsorship")
        var sponsorship: Any?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("urls")
        var urls: UrlsXX?,
        @SerializedName("user")
        var user: UserX?,
        @SerializedName("width")
        var width: Int?
)

data class Category(
        @SerializedName("pretty_slug")
        var prettySlug: String?,
        @SerializedName("slug")
        var slug: String?
)

data class Subcategory(
        @SerializedName("pretty_slug")
        var prettySlug: String?,
        @SerializedName("slug")
        var slug: String?
)

data class Type(
        @SerializedName("pretty_slug")
        var prettySlug: String?,
        @SerializedName("slug")
        var slug: String?
)

data class LinksXXXX(
        @SerializedName("download")
        var download: String?,
        @SerializedName("download_location")
        var downloadLocation: String?,
        @SerializedName("html")
        var html: String?,
        @SerializedName("self")
        var self: String?
)

data class UrlsXX(
        @SerializedName("full")
        var full: String?,
        @SerializedName("raw")
        var raw: String?,
        @SerializedName("regular")
        var regular: String?,
        @SerializedName("small")
        var small: String?,
        @SerializedName("thumb")
        var thumb: String?
)

data class UserX(
        @SerializedName("accepted_tos")
        var acceptedTos: Boolean?,
        @SerializedName("bio")
        var bio: String?,
        @SerializedName("first_name")
        var firstName: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("instagram_username")
        var instagramUsername: String?,
        @SerializedName("last_name")
        var lastName: String?,
        @SerializedName("links")
        var links: LinksXXXXX?,
        @SerializedName("location")
        var location: String?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("portfolio_url")
        var portfolioUrl: String?,
        @SerializedName("profile_image")
        var profileImage: ProfileImageX?,
        @SerializedName("total_collections")
        var totalCollections: Int?,
        @SerializedName("total_likes")
        var totalLikes: Int?,
        @SerializedName("total_photos")
        var totalPhotos: Int?,
        @SerializedName("twitter_username")
        var twitterUsername: String?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("username")
        var username: String?
)

data class LinksXXXXX(
        @SerializedName("followers")
        var followers: String?,
        @SerializedName("following")
        var following: String?,
        @SerializedName("html")
        var html: String?,
        @SerializedName("likes")
        var likes: String?,
        @SerializedName("photos")
        var photos: String?,
        @SerializedName("portfolio")
        var portfolio: String?,
        @SerializedName("self")
        var self: String?
)

data class ProfileImageX(
        @SerializedName("large")
        var large: String?,
        @SerializedName("medium")
        var medium: String?,
        @SerializedName("small")
        var small: String?
)

data class LinksXXXXXX(
        @SerializedName("followers")
        var followers: String?,
        @SerializedName("following")
        var following: String?,
        @SerializedName("html")
        var html: String?,
        @SerializedName("likes")
        var likes: String?,
        @SerializedName("photos")
        var photos: String?,
        @SerializedName("portfolio")
        var portfolio: String?,
        @SerializedName("self")
        var self: String?
)

data class ProfileImageXX(
        @SerializedName("large")
        var large: String?,
        @SerializedName("medium")
        var medium: String?,
        @SerializedName("small")
        var small: String?
)

data class Sponsor(
        @SerializedName("accepted_tos")
        var acceptedTos: Boolean?,
        @SerializedName("bio")
        var bio: String?,
        @SerializedName("first_name")
        var firstName: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("instagram_username")
        var instagramUsername: String?,
        @SerializedName("last_name")
        var lastName: Any?,
        @SerializedName("links")
        var links: LinksXXXXXXX?,
        @SerializedName("location")
        var location: Any?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("portfolio_url")
        var portfolioUrl: String?,
        @SerializedName("profile_image")
        var profileImage: ProfileImageXXX?,
        @SerializedName("total_collections")
        var totalCollections: Int?,
        @SerializedName("total_likes")
        var totalLikes: Int?,
        @SerializedName("total_photos")
        var totalPhotos: Int?,
        @SerializedName("twitter_username")
        var twitterUsername: String?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("username")
        var username: String?
)

data class LinksXXXXXXX(
        @SerializedName("followers")
        var followers: String?,
        @SerializedName("following")
        var following: String?,
        @SerializedName("html")
        var html: String?,
        @SerializedName("likes")
        var likes: String?,
        @SerializedName("photos")
        var photos: String?,
        @SerializedName("portfolio")
        var portfolio: String?,
        @SerializedName("self")
        var self: String?
)

data class ProfileImageXXX(
        @SerializedName("large")
        var large: String?,
        @SerializedName("medium")
        var medium: String?,
        @SerializedName("small")
        var small: String?
)

data class SourceX(
        @SerializedName("ancestry")
        var ancestry: AncestryX?,
        @SerializedName("cover_photo")
        var coverPhoto: CoverPhotoXX?,
        @SerializedName("description")
        var description: String?,
        @SerializedName("meta_description")
        var metaDescription: String?,
        @SerializedName("meta_title")
        var metaTitle: String?,
        @SerializedName("subtitle")
        var subtitle: String?,
        @SerializedName("title")
        var title: String?
)

data class AncestryX(
        @SerializedName("category")
        var category: CategoryX?,
        @SerializedName("subcategory")
        var subcategory: SubcategoryX?,
        @SerializedName("type")
        var type: TypeX?
)

data class CoverPhotoXX(
        @SerializedName("alt_description")
        var altDescription: String?,
        @SerializedName("blur_hash")
        var blurHash: String?,
        @SerializedName("categories")
        var categories: List<Any>?,
        @SerializedName("color")
        var color: String?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("current_user_collections")
        var currentUserCollections: List<Any>?,
        @SerializedName("description")
        var description: String?,
        @SerializedName("height")
        var height: Int?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("liked_by_user")
        var likedByUser: Boolean?,
        @SerializedName("likes")
        var likes: Int?,
        @SerializedName("links")
        var links: LinksXXXXXXXX?,
        @SerializedName("promoted_at")
        var promotedAt: String?,
        @SerializedName("sponsorship")
        var sponsorship: Any?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("urls")
        var urls: UrlsXXX?,
        @SerializedName("user")
        var user: UserXXX?,
        @SerializedName("width")
        var width: Int?
)

data class CategoryX(
        @SerializedName("pretty_slug")
        var prettySlug: String?,
        @SerializedName("slug")
        var slug: String?
)

data class SubcategoryX(
        @SerializedName("pretty_slug")
        var prettySlug: String?,
        @SerializedName("slug")
        var slug: String?
)

data class TypeX(
        @SerializedName("pretty_slug")
        var prettySlug: String?,
        @SerializedName("slug")
        var slug: String?
)

data class LinksXXXXXXXX(
        @SerializedName("download")
        var download: String?,
        @SerializedName("download_location")
        var downloadLocation: String?,
        @SerializedName("html")
        var html: String?,
        @SerializedName("self")
        var self: String?
)

data class UrlsXXX(
        @SerializedName("full")
        var full: String?,
        @SerializedName("raw")
        var raw: String?,
        @SerializedName("regular")
        var regular: String?,
        @SerializedName("small")
        var small: String?,
        @SerializedName("thumb")
        var thumb: String?
)

data class UserXXX(
        @SerializedName("accepted_tos")
        var acceptedTos: Boolean?,
        @SerializedName("bio")
        var bio: String?,
        @SerializedName("first_name")
        var firstName: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("instagram_username")
        var instagramUsername: String?,
        @SerializedName("last_name")
        var lastName: String?,
        @SerializedName("links")
        var links: LinksXXXXXXXXX?,
        @SerializedName("location")
        var location: String?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("portfolio_url")
        var portfolioUrl: String?,
        @SerializedName("profile_image")
        var profileImage: ProfileImageXXXX?,
        @SerializedName("total_collections")
        var totalCollections: Int?,
        @SerializedName("total_likes")
        var totalLikes: Int?,
        @SerializedName("total_photos")
        var totalPhotos: Int?,
        @SerializedName("twitter_username")
        var twitterUsername: String?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("username")
        var username: String?
)

data class LinksXXXXXXXXX(
        @SerializedName("followers")
        var followers: String?,
        @SerializedName("following")
        var following: String?,
        @SerializedName("html")
        var html: String?,
        @SerializedName("likes")
        var likes: String?,
        @SerializedName("photos")
        var photos: String?,
        @SerializedName("portfolio")
        var portfolio: String?,
        @SerializedName("self")
        var self: String?
)

data class ProfileImageXXXX(
        @SerializedName("large")
        var large: String?,
        @SerializedName("medium")
        var medium: String?,
        @SerializedName("small")
        var small: String?
)

data class LinksXXXXXXXXXX(
        @SerializedName("followers")
        var followers: String?,
        @SerializedName("following")
        var following: String?,
        @SerializedName("html")
        var html: String?,
        @SerializedName("likes")
        var likes: String?,
        @SerializedName("photos")
        var photos: String?,
        @SerializedName("portfolio")
        var portfolio: String?,
        @SerializedName("self")
        var self: String?
)

data class ProfileImageXXXXX(
        @SerializedName("large")
        var large: String?,
        @SerializedName("medium")
        var medium: String?,
        @SerializedName("small")
        var small: String?
)