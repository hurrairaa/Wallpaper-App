package com.rigrex.wallpaperapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rigrex.wallpaperapp.database.dataModels.Favorite
import retrofit2.http.GET

@Dao
interface FavoriteDao {
    @Insert
    fun addToFav(favorite: Favorite)

    @Query("DELETE FROM wallpapers_favorite WHERE image_id == :favId")
    fun deleteFromFav(favId: String)

    @Query("SELECT * FROM wallpapers_favorite LIMIT 10 OFFSET 0")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Query("SELECT * FROM wallpapers_favorite WHERE (image_id==:id) LIMIT 1")
    fun getFavorite(id: String): Favorite?
}