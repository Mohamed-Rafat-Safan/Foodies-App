package com.example.foodapp.di.diDb

import com.example.data.db.MealsDao
import com.example.data.repo.repoDb.MealsDbRepo
import com.example.domain.repo.repoDb.MealsDbRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoDbModule {

    @Provides
    fun provideMealDbRepo(mealsDao: MealsDao): MealsDbRepo {
        return MealsDbRepoImpl(mealsDao)
    }


}