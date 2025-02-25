package com.example.data.repo.repoDb

import com.example.data.model.Category
import com.example.data.model.Meal
import com.example.data.model.MealCategory

interface MealsDbRepo {
    suspend fun insertOrUpdateMeal(meal: Meal)

    suspend fun insertOrUpdateMealCategory(mealCategory: MealCategory)

    suspend fun insertOrUpdateCategories(category: Category)

    suspend fun getAllMeals(): List<Meal>

    suspend fun getMealsCategoryFromDb(): List<MealCategory>

    suspend fun getAllCategoriesFromDb(): List<Category>

    suspend fun deleteMeal(meal: Meal)

    suspend fun searchMealByIdFromDb(mealId: String): Meal
}