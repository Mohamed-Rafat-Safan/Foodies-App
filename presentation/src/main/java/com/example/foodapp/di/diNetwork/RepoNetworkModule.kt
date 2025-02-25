package com.example.foodapp.di.diNetwork

import com.example.data.network.MealsApi
import com.example.data.repo.repoNetwork.MealsNetworkRepo
import com.example.domain.repo.repoNetwork.MealsNetworkRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoNetworkModule {

    @Provides
    fun provideRepo(mealsApi: MealsApi): MealsNetworkRepo {
        return MealsNetworkRepoImpl(mealsApi)
    }

}