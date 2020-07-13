package com.geeklabs.spiffyshow.utils

import androidx.room.TypeConverter
import com.geeklabs.spiffyshow.data.remote.models.Param
import com.geeklabs.spiffyshow.models.FileMetaData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class Converters {

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromArray(array: Array<Int>): String {
        return Gson().toJson(array)
    }

    @TypeConverter
    fun fromString(value: String): Array<Int> {
        val listType = object : TypeToken<Array<Int>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromParamList(list: List<Param>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toParamList(value: String): List<Param> {
        val listType = object : TypeToken<List<Param>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromIntList(list: List<Int>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromAddressList(list: List<com.geeklabs.spiffyshow.data.local.models.user.Address>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toAddressList(value: String): List<com.geeklabs.spiffyshow.data.local.models.user.Address> {
        val listType =
            object : TypeToken<List<com.geeklabs.spiffyshow.data.local.models.user.Address>>() {
            }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromFileMetaData(data: FileMetaData): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun toFileMetaData(value: String): FileMetaData {
        val listType = object : TypeToken<FileMetaData>() {
        }.type
        return Gson().fromJson(value, listType)
    }
}