package com.example.pokeapp.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokeapp.data.source.network.entity.TypeDetailsResponse
import com.example.pokeapp.domain.entity.PokeTypeDetails
import java.util.Date
import com.example.pokeapp.domain.entity.PokeType as DomainPokeType

@Entity
data class PokeType(
    @PrimaryKey val name: String,
    var insertionDate: Date = Date(),
    val doubleDamageFrom: List<PokeType> = emptyList(),
    val doubleDamageTo: List<PokeType> = emptyList(),
    val halfDamageFrom: List<PokeType> = emptyList(),
    val halfDamageTo: List<PokeType> = emptyList(),
    val noDamageFrom: List<PokeType> = emptyList(),
    val noDamageTo: List<PokeType> = emptyList()
) {
    fun toDomainEntity() =
        PokeTypeDetails(
            name = name,
            attackerTypesRelation = doubleDamageTo.map {
                Pair(DomainPokeType(it.name), PokeTypeDetails.DOUBLE_DAMAGE_FACTOR)
            } + halfDamageTo.map {
                Pair(DomainPokeType(it.name), PokeTypeDetails.HALF_DAMAGE_FACTOR)
            } + noDamageTo.map {
                Pair(DomainPokeType(it.name), PokeTypeDetails.NO_DAMAGE_FACTOR)
            },
            defenderTypesRelation = doubleDamageFrom.map {
                Pair(DomainPokeType(it.name), PokeTypeDetails.DOUBLE_DAMAGE_FACTOR)
            } + halfDamageFrom.map {
                Pair(DomainPokeType(it.name), PokeTypeDetails.HALF_DAMAGE_FACTOR)
            } + noDamageFrom.map {
                Pair(DomainPokeType(it.name), PokeTypeDetails.NO_DAMAGE_FACTOR)
            },
        )

    companion object {
        fun fromNetworkEntity(networkEntity: TypeDetailsResponse): PokeType =
            networkEntity.run {
                PokeType(
                    name = name,
                    doubleDamageFrom = damageRelations.doubleDamageFrom.map { PokeType(it.name) },
                    doubleDamageTo = damageRelations.doubleDamageTo.map { PokeType(it.name) },
                    halfDamageFrom = damageRelations.halfDamageFrom.map { PokeType(it.name) },
                    halfDamageTo = damageRelations.halfDamageTo.map { PokeType(it.name) },
                    noDamageFrom = damageRelations.noDamageFrom.map { PokeType(it.name) },
                    noDamageTo = damageRelations.noDamageTo.map { PokeType(it.name) }
                )
            }

    }
}
