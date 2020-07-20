package com.geeklabs.spiffyshow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.geeklabs.spiffyshow.data.local.models.item.Original
import io.reactivex.Flowable

@Dao
interface OriginalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Original>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(original: Original)

    @Query("SELECT * FROM Original")
    fun getAll(): Flowable<MutableList<Original>>

    @Query("SELECT * FROM Original WHERE catId=:categoryId")
    fun getAllById(categoryId: Long): Flowable<MutableList<Original>>

    @Query("DELETE FROM Original WHERE id=:id")
    fun deleteItem(id: Long)
}