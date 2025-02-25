package com.example.foodapp.di.diNetwork

import com.example.data.repo.repoNetwork.MealsNetworkRepo
import com.example.domain.usecase.usecaseNetwork.MealsNetworkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseNetworkModule {

    @Provides
    fun provideHomeUseCase(mealsNetworkRepo: MealsNetworkRepo): MealsNetworkUseCase {
        return MealsNetworkUseCase(mealsNetworkRepo)
    }
}