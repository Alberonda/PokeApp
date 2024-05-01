package com.example.pokeapp.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokeapp.data.source.local.entity.LocalAllAbilitiesResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface PokeAbilityLocalDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ability: LocalAllAbilitiesResponse)

    @Query("SELECT * FROM localAllAbilitiesResponse")
    fun getAll(): Flow<LocalAllAbilitiesResponse>
}