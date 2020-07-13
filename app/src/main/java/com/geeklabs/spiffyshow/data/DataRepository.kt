package com.geeklabs.spiffyshow.data

import com.geeklabs.spiffyshow.data.local.LocalDataSource
import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.data.local.models.location.DeviceLocation
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.data.remote.RemoteDataSource
import javax.inject.Inject

open class DataRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    // Local calls
    fun saveItemsInLocal(items: MutableList<Item>) = localDataSource.saveItems(items)
    fun getItemsFromLocal() = localDataSource.getItems()
    fun getItemsByCategoryId(categoryId: Long) = localDataSource.getItemsByCategoryId(categoryId)
    fun deleteItemLocal(id: Long) = localDataSource.deleteItem(id)
    fun saveTrimInLocal(items: MutableList<Trim>) = localDataSource.saveTrims(items)
    fun getTrimsFromLocal() = localDataSource.getTrims()
    fun deleteTrimFromLocal(id: Long) = localDataSource.deleteTrim(id)
    fun saveUser(user: User) = localDataSource.save(user)
    fun getUsersCount() = localDataSource.getUserCount()
    fun getUserById(id: Long) = localDataSource.getUserById(id)
    fun getDeviceLocation() = localDataSource.getDeviceLocation()
    fun saveDeviceLocation(deviceLocation: DeviceLocation) =
        localDataSource.saveDeviceLocation(deviceLocation)

    // Remote calls
    fun login(phoneNumber: String) = remoteDataSource.login(phoneNumber)
    fun saveUpdateUser(user: User) = remoteDataSource.saveUpdateUser(user)
    fun getItemsFromRemote() = remoteDataSource.getItemsFromRemote()
    fun addTokenInHeaderInterceptor(registrationToken: String) =
        remoteDataSource.addTokenInHeaderInterceptor(registrationToken)

    fun addBasicAuthHeaderInterceptor(userName: String, password: String) =
        remoteDataSource.addBasicAuthHeaderInterceptor(userName, password)
}