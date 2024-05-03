package com.example.pokeapp.di

import com.example.pokeapp.data.source.network.PokeAbilityNetworkDataSource
import com.example.pokeapp.data.source.network.PokeTypeNetworkDataSource
import com.example.pokeapp.data.source.network.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ApiModule {

    @Provides
    fun providePokeTypeNetworkDataSource(): PokeTypeNetworkDataSource = RetrofitInstance.getInstance()

    @Provides
    fun providePokeAbilityNetworkDataSource(): PokeAbilityNetworkDataSource = RetrofitInstance.getInstance()
}