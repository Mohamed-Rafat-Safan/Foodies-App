package com.example.foodapp.di.diDb

import com.example.data.repo.repoDb.MealsDbRepo
import com.example.domain.usecase.usecaseDb.MealsDbUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseDbModule {

    @Provides
    fun provideMealDbUseCase(mealsDbRepo: MealsDbRepo): MealsDbUseCase {
        return MealsDbUseCase(mealsDbRepo)
    }

}