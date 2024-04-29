package com.example.pokeapp.di

import com.example.pokeapp.data.repository.poketype.PokeTypeRepository
import com.example.pokeapp.data.repository.poketype.PokeTypeDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providePokeTypeRepository(impl: PokeTypeDataSource): PokeTypeRepository
}