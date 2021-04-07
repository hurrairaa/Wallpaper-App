package com.rigrex.wallpaperapp.database.dao

import androidx.room.*
import com.rigrex.wallpaperapp.database.dataModels.TimeModel

@Dao
interface TimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTime(timeModel: TimeModel)

    @Query("SELECT * FROM auto_time")
    fun getAllTimes(): List<TimeModel>

    @Query("DELETE FROM auto_time WHERE (t_id ==:id AND title ==:title)")
    fun deleteTime(id: Int, title: String)
}