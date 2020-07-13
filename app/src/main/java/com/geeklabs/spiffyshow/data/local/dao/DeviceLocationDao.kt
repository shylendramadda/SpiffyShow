package com.geeklabs.spiffyshow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.geeklabs.spiffyshow.data.local.models.location.DeviceLocation
import io.reactivex.Single

@Dao
interface DeviceLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(location: DeviceLocation)

    @Query("SELECT * FROM DeviceLocation")
    fun get(): Single<DeviceLocation>
}