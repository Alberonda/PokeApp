package com.example.pokeapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokeapp.data.source.local.converters.DateConverters
import com.example.pokeapp.data.source.local.converters.ListConverters
import com.example.pokeapp.data.source.local.entity.PokeType

@Database(entities = [PokeType::class], version = 1, exportSchema = false)
@TypeConverters(
    ListConverters::class,
    DateConverters::class
)
abstract class PokeTypeDatabase : RoomDatabase() {
    abstract fun pokeTypeDao(): PokeTypeLocalDataSource

    companion object {
        @Volatile
        private var INSTANCE: PokeTypeDatabase? = null

        fun getDatabase(context: Context): PokeTypeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    PokeTypeDatabase::class.java,
                    "poke_type_db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance
            }
        }

        const val CACHE_TIME_MINUTES = 1440
    }
}