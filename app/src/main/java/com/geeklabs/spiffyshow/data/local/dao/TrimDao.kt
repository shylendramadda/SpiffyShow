package com.geeklabs.spiffyshow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import io.reactivex.Flowable

@Dao
interface TrimDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Trim>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(item: Trim)

    @Query("SELECT * FROM Trim")
    fun getAll(): Flowable<MutableList<Trim>>

    @Query("SELECT * FROM Item WHERE catId=:categoryId")
    fun getAllById(categoryId: Long): Flowable<MutableList<Trim>>

    @Query("DELETE FROM Trim WHERE id=:id")
    fun deleteItem(id: Long)
}