package com.example.data.repo.repoNetwork

import com.example.data.model.MealCategoryResponse
import com.example.data.model.MealsCategoriesResponse
import com.example.data.model.ListMealsResponse
import retrofit2.Response

interface MealsNetworkRepo {
    suspend fun getRandomMeal(): Response<ListMealsResponse>

    suspend fun getMealById(mealId: String): Response<ListMealsResponse>

    suspend fun getMealCategory(categoryName: String): Response<MealCategoryResponse>

    suspend fun getAllMealsCategory(): Response<MealsCategoriesResponse>

    suspend fun searchMealByName(mealName: String): Response<ListMealsResponse>
}