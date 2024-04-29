package com.example.pokeapp.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokeapp.data.source.local.entity.PokeType
import kotlinx.coroutines.flow.Flow

@Dao
interface PokeTypeLocalDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokeType(pokeType: PokeType)

    @Query("SELECT * FROM poketype WHERE name = :name LIMIT 1")
    fun getPokeTypeByName(name: String) : Flow<PokeType?>

    @Query("SELECT * FROM poketype")
    fun getAllPokeType() : Flow<List<PokeType>>
}