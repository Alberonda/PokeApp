package com.example.pokeapp.presentation.typeslanding.entity

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.pokeapp.R

enum class PokeTypeUiResources(
    val typeName: String,
    @DrawableRes val imageResource: Int,
    @ColorRes val color: Int,
    @StringRes val localisedName: Int
) {
    NORMAL(
        "normal",
        R.drawable.ic_type_normal,
        R.color.normal,
        R.string.normal
    ),
    DARK(
        "dark",
        R.drawable.ic_type_dark,
        R.color.dark,
        R.string.dark
    ),
    ELECTRIC(
        "electric",
        R.drawable.ic_type_electric,
        R.color.electric,
        R.string.electric
    ),
    FAIRY(
        "fairy",
        R.drawable.ic_type_fairy,
        R.color.fairy,
        R.string.fairy
    ),
    FIGHTING(
        "fighting",
        R.drawable.ic_type_fighting,
        R.color.fighting,
        R.string.fighting
    ),
    GROUND(
        "ground",
        R.drawable.ic_type_ground,
        R.color.ground,
        R.string.ground
    ),
    ICE(
        "ice",
        R.drawable.ic_type_ice,
        R.color.ice,
        R.string.ice
    ),
    POISON(
        "poison",
        R.drawable.ic_type_poison,
        R.color.poison,
        R.string.poison
    ),
    PSYCHIC(
        "psychic",
        R.drawable.ic_type_psychic,
        R.color.psychic,
        R.string.psychic
    ),
    ROCK(
        "rock",
        R.drawable.ic_type_rock,
        R.color.rock,
        R.string.rock
    ),
    STEEL(
        "steel",
        R.drawable.ic_type_steel,
        R.color.steel,
        R.string.steel
    ),
    WATER(
        "water",
        R.drawable.ic_type_water,
        R.color.water,
        R.string.water
    ),
    FlYING(
        "flying",
        R.drawable.ic_type_flying,
        R.color.flying,
        R.string.flying
    ),
    BUG(
        "bug",
        R.drawable.ic_type_bug,
        R.color.bug,
        R.string.bug
    ),
    GHOST(
        "ghost",
        R.drawable.ic_type_ghost,
        R.color.ghost,
        R.string.ghost
    ),
    FIRE(
        "fire",
        R.drawable.ic_type_fire,
        R.color.fire,
        R.string.fire
    ),
    GRASS(
        "grass",
        R.drawable.ic_type_grass,
        R.color.grass,
        R.string.grass
    ),
    DRAGON(
        "dragon",
        R.drawable.ic_type_dragon,
        R.color.dragon,
        R.string.dragon
    )
}