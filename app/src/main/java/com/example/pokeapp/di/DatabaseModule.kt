package com.example.pokeapp.di

import android.content.Context
import androidx.room.Room
import com.example.pokeapp.data.source.local.PokeTypeDatabase
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
        pokeTypeDatabase: PokeTypeDatabase
    ): PokeTypeLocalDataSource = pokeTypeDatabase.pokeTypeDao()

    @Provides
    @Singleton
    fun providePokeTypeDatabase(@ApplicationContext context: Context): PokeTypeDatabase =
        PokeTypeDatabase.getDatabase(context)
}