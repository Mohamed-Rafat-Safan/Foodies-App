package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.Category
import com.example.data.model.Meal
import com.example.data.model.MealCategory
import com.example.data.utils.Constant.CATEGORIES_TABLE_NAME
import com.example.data.utils.Constant.MEAL_CATEGORY_TABLE_NAME
import com.example.data.utils.Constant.MEAL_TABLE_NAME

@Dao
interface MealsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateMeal(meal: Meal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateMealCategory(mealCategory: MealCategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCategories(category: Category)

    @Query("SELECT * FROM $MEAL_TABLE_NAME")
    suspend fun getAllMeals(): List<Meal>

    @Query("SELECT * FROM $MEAL_CATEGORY_TABLE_NAME")
    suspend fun getMealsCategoryFromDb(): List<MealCategory>

    @Query("SELECT * FROM $CATEGORIES_TABLE_NAME")
    suspend fun getAllCategoriesFromDb(): List<Category>

    @Delete
    suspend fun deleteMeal(meal: Meal)


    @Query("SELECT * FROM MEAL_TABLE WHERE idMeal LIKE :mealId")
    suspend fun searchMealByIdFromDb(mealId: String): Meal


//    @Query("DELETE FROM MEAL_TABLE WHERE idMeal = :id")
//    suspend fun deleteAllNotes()

}