package com.example.pokeapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokeapp.data.source.local.converters.DateConverters
import com.example.pokeapp.data.source.local.converters.CustomConverters
import com.example.pokeapp.data.source.local.entity.LocalAllAbilitiesResponse
import com.example.pokeapp.data.source.local.entity.PokeType

@Database(
    entities = [PokeType::class, LocalAllAbilitiesResponse::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    CustomConverters::class,
    DateConverters::class
)
abstract class PokeAppDatabase : RoomDatabase() {
    abstract fun pokeTypeDao(): PokeTypeLocalDataSource
    abstract fun pokeAbilityDao(): PokeAbilityLocalDataSource

    companion object {
        @Volatile
        private var INSTANCE: PokeAppDatabase? = null

        fun getDatabase(context: Context): PokeAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    PokeAppDatabase::class.java,
                    "poke_app_db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance
            }
        }

        const val CACHE_TIME_MINUTES = 1440
    }
}