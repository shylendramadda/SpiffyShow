package com.geeklabs.spiffyshow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.geeklabs.spiffyshow.data.local.models.user.User
import io.reactivex.Single

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT COUNT(*) from User")
    fun getUsersCount(): Single<Int>

    @Query("SELECT * FROM User WHERE id=:id")
    fun getById(id: Long): Single<User>
}