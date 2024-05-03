package com.example.pokeapp.di

import android.content.Context
import com.example.pokeapp.data.source.local.PokeAbilityLocalDataSource
import com.example.pokeapp.data.source.local.PokeAppDatabase
import com.example.pokeapp.data.source.local.PokeTypeLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun providePokeTypeLocalDataSource(
        pokeTypeDatabase: PokeAppDatabase
    ): PokeTypeLocalDataSource = pokeTypeDatabase.pokeTypeDao()

    @Provides
    fun providePokeAbilityLocalDataSource(
        pokeTypeDatabase: PokeAppDatabase
    ): PokeAbilityLocalDataSource = pokeTypeDatabase.pokeAbilityDao()


    @Provides
    @Singleton
    fun providePokeTypeDatabase(@ApplicationContext context: Context): PokeAppDatabase =
        PokeAppDatabase.getDatabase(context)
}