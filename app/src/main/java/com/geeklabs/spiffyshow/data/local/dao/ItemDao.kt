package com.geeklabs.spiffyshow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.geeklabs.spiffyshow.data.local.models.item.Item
import io.reactivex.Flowable

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Item>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(item: Item)

    @Query("SELECT * FROM Item")
    fun getAll(): Flowable<MutableList<Item>>

    @Query("SELECT * FROM Item WHERE catId=:categoryId")
    fun getAllById(categoryId: Long): Flowable<MutableList<Item>>

    @Query("DELETE FROM Item WHERE id=:id")
    fun deleteItem(id: Long)
}