package com.example.foodapp.di.diDb

import android.app.Application
import com.example.data.db.MealsDao
import com.example.data.db.MealsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MealsDatabase {
        return MealsDatabase.getDatabase(app)
    }


    @Provides
    @Singleton
    fun provideMealsDao(database: MealsDatabase): MealsDao {
        return database.mealsDao()
    }

}