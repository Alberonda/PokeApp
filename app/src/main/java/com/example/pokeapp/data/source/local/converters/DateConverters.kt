package com.example.pokeapp.data.source.local.converters

import androidx.room.TypeConverter
import java.util.Date

object DateConverters {

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}