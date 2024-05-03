package com.example.pokeapp.di

import com.example.pokeapp.domain.usecase.getallabilities.GetAbilityDetailsUseCase
import com.example.pokeapp.domain.usecase.getallabilities.GetAbilityDetailsUseCaseImpl
import com.example.pokeapp.domain.usecase.getallabilities.GetAllAbilitiesUseCase
import com.example.pokeapp.domain.usecase.getallabilities.GetAllAbilitiesUseCaseImpl
import com.example.pokeapp.domain.usecase.getalltypes.GetAllTypesUseCase
import com.example.pokeapp.domain.usecase.getalltypes.GetAllTypesUseCaseImpl
import com.example.pokeapp.domain.usecase.gettypedetails.GetTypeDetailsUseCase
import com.example.pokeapp.domain.usecase.gettypedetails.GetTypeDetailsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun provideGetTypeDetailsUseCase(impl: GetTypeDetailsUseCaseImpl): GetTypeDetailsUseCase

    @Binds
    abstract fun provideGetAllTypesUseCase(impl: GetAllTypesUseCaseImpl): GetAllTypesUseCase

    @Binds
    abstract fun provideGetAllAbilitiesUseCase(impl: GetAllAbilitiesUseCaseImpl): GetAllAbilitiesUseCase

    @Binds
    abstract fun provideGetAbilityDetailsUseCase(impl: GetAbilityDetailsUseCaseImpl): GetAbilityDetailsUseCase

}