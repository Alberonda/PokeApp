package com.example.pokeapp.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokeapp.data.source.network.entity.TypeDetailsResponse
import java.util.Date
import com.example.pokeapp.data.source.network.entity.NetworkAllAbilitiesResponse as NetworkAllAbilitiesResponse

@Entity
data class LocalAllAbilitiesResponse(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var insertionDate: Date = Date(),
    val value: NetworkAllAbilitiesResponse
) {
    companion object {
        fun fromNetworkEntity(networkEntity: NetworkAllAbilitiesResponse): LocalAllAbilitiesResponse =
            networkEntity.run {
                LocalAllAbilitiesResponse(
                    value = networkEntity
                )
            }
    }
}