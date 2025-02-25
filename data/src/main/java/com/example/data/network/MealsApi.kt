package com.example.data.network

import com.example.data.model.MealsCategoriesResponse
import com.example.data.model.MealCategoryResponse
import com.example.data.model.ListMealsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealsApi {
    @GET("random.php")
    suspend fun getRandomMeal(): Response<ListMealsResponse>

    @GET("lookup.php?")
    suspend fun getMealById(@Query("i") mealId: String): Response<ListMealsResponse>

    @GET("filter.php?")
    suspend fun getMealCategory(@Query("c") categoryName: String): Response<MealCategoryResponse>

    @GET("categories.php")
    suspend fun getAllMealsCategory(): Response<MealsCategoriesResponse>

    @GET("search.php?")
    suspend fun searchMealByName(@Query("s") mealName: String): Response<ListMealsResponse>
}