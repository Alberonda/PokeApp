package com.example.pokeapp.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokeapp.data.source.network.entity.NetworkAbilityDetailsResponse
import java.util.Date

@Entity
data class LocalAbilityDetailsResponse(
    @PrimaryKey val abilityName: String,
    var insertionDate: Date = Date(),
    val value: NetworkAbilityDetailsResponse
) {
    companion object {
        fun fromNetworkEntity(networkEntity: NetworkAbilityDetailsResponse): LocalAbilityDetailsResponse =
            networkEntity.run {
                LocalAbilityDetailsResponse(
                    abilityName = this.name,
                    value = this
                )
            }
    }
}