package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.Category
import com.example.data.model.Meal
import com.example.data.model.MealCategory
import com.example.data.utils.Constant.DATABASE_NAME

@Database(
    entities = [Meal::class, MealCategory::class, Category::class],
    version = 1,
    exportSchema = false
)
abstract class MealsDatabase : RoomDatabase() {
    abstract fun mealsDao(): MealsDao

    companion object {
        // this annotation make the field immediately visible to other threads
        @Volatile
        private var INSTANCE: MealsDatabase? = null

        fun getDatabase(context: Context): MealsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            // if the instance is null we are creating a new instance
            // synchronized block will prevent other thread from accessing this instance
            // fallbackToDestructiveMigration() to update the database if version is changed
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    MealsDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration().build()


                INSTANCE = instance
                return instance
            }
        }

    }
}