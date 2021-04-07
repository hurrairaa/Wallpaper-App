package com.rigrex.wallpaperapp.network

import com.rigrex.wallpaperapp.database.unsplash.unsplashDescriptionData.UnSplashModel
import com.shehzad.wallpaperapp.pixabey.HitModel
import com.shehzad.wallpaperapp.unsplash.UnsplashModel
import com.shehzad.wallpaperapp.unsplash.UnsplashSearchModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface ApiInterface {
    @GET("api/")
    fun searchPixabay(
            @Query("key") key: String = ApiClient.API_KEY_PIXABAY,
            @Query("q") q: String = "",
            @Query("lang") lang: String = "en",
            @Query("orientation") orientation: String = "vertical",
            @Query("category") category: String,
            @Query("page") page: String,
            @Query("per_page") per_page: String = "20",
            @Query("safesearch") safeSearch: Boolean = true
    ): Observable<HitModel>

    @GET("photos/")
    fun getUnSplashPhotos(
            @Query("client_id") key: String = ApiClient.API_KEY_UNSPLASH,
            @Query("order_by") category: String = "latest",
            @Query("page") page: String,
            @Query("per_page") per_page: String = "20"
    ): Observable<List<UnsplashModel>>

    @GET
    fun unSplashImageDetail(
            @Url photo_id: String,
            @Query("client_id") key: String = ApiClient.API_KEY_UNSPLASH,
    ): Observable<UnSplashModel>

    @GET("search/photos/")
    fun searchUnSplashPhotos(
            @Query("client_id") key: String = ApiClient.API_KEY_UNSPLASH,
            @Query("page") page: String,
            @Query("per_page") perPage: String = "20",
            @Query("query") query: String,
            @Query("orientation") orientation: String = "portrait"
    ): Observable<UnsplashSearchModel>

}
