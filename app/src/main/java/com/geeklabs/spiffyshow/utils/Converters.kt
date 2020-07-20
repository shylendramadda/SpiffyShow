package com.geeklabs.spiffyshow.utils

import androidx.room.TypeConverter
import com.geeklabs.spiffyshow.data.local.models.item.Comment
import com.geeklabs.spiffyshow.data.local.models.item.Like
import com.geeklabs.spiffyshow.data.local.models.item.Original
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
    fun fromOriginal(data: Original): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun toOriginal(value: String): Original {
        val type = object : TypeToken<Original>() {
        }.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromCommentList(data: List<Comment>): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun toCommentList(value: String): List<Comment> {
        val listType = object : TypeToken<List<Comment>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromLikeList(data: List<Like>): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun toLikeList(value: String): List<Like> {
        val listType = object : TypeToken<List<Like>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromFileMetaData(data: FileMetaData): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun toFileMetaData(value: String): FileMetaData {
        val type = object : TypeToken<FileMetaData>() {
        }.type
        return Gson().fromJson(value, type)
    }
}