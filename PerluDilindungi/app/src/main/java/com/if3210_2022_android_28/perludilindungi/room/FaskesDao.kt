package com.if3210_2022_android_28.perludilindungi.room

import androidx.room.*
import com.if3210_2022_android_28.perludilindungi.model.Faskes

@Dao
interface FaskesDao {

    @Insert
    suspend fun addFaskes(faskes: Faskes)

    @Update
    suspend fun updateFaskse(faskes: Faskes)

    @Delete
    suspend fun deleteFaskes(faskes: Faskes)

    @Query("SELECT * FROM faskes")
    suspend fun getFaskes() : List<Faskes>

    @Query("SELECT * FROM faskes WHERE id = :faskesID")
    suspend fun getFaskes(faskesID: Int) : List<Faskes>

    @Query("UPDATE faskes SET bookmarked = :value WHERE id = :faskesID")
    suspend fun setBookmark(faskesID: Int, value: Int)

}