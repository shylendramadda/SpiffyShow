package com.geeklabs.spiffyshow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.geeklabs.spiffyshow.data.local.dao.DeviceLocationDao
import com.geeklabs.spiffyshow.data.local.dao.OriginalDao
import com.geeklabs.spiffyshow.data.local.dao.TrimDao
import com.geeklabs.spiffyshow.data.local.dao.UserDao
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.location.DeviceLocation
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.utils.Converters

@Database(
    entities = [User::class, DeviceLocation::class, Original::class, Trim::class],
    version = 1, exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun deviceLocationDao(): DeviceLocationDao
    abstract fun itemDao(): OriginalDao
    abstract fun trimDao(): TrimDao
}