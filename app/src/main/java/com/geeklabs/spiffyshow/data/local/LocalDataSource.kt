package com.geeklabs.spiffyshow.data.local

import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.location.DeviceLocation
import com.geeklabs.spiffyshow.data.local.models.user.User
import javax.inject.Inject

open class LocalDataSource @Inject constructor(private val appDatabaseWrapper: AppDatabaseWrapper) {

    private val appDatabase: AppDatabase
        get() = appDatabaseWrapper.getAppDatabase()

    fun getUserCount() = appDatabase.userDao().getUsersCount()
    fun save(user: User) = appDatabase.userDao().insert(user)
    fun getUserById(id: Long) = appDatabase.userDao().getById(id)
    fun saveDeviceLocation(deviceLocation: DeviceLocation) =
        appDatabase.deviceLocationDao().save(deviceLocation)

    //Items
    fun saveOriginals(originals: MutableList<Original>) = appDatabase.itemDao().insert(originals.toList())
    fun getOriginals() = appDatabase.itemDao().getAll()
    fun getOriginalsByCategoryId(categoryId: Long) = appDatabase.itemDao().getAllById(categoryId)
    fun deleteOriginal(id: Long) = appDatabase.itemDao().deleteItem(id)

    //Trims
    fun saveTrims(items: MutableList<Trim>) = appDatabase.trimDao().insert(items.toList())
    fun deleteTrim(id: Long) = appDatabase.trimDao().deleteItem(id)
    fun getTrims() = appDatabase.trimDao().getAll()

    //Location
    fun getDeviceLocation() = appDatabase.deviceLocationDao().get()
}