package com.example.domain.repo.repoNetwork

import com.example.data.model.ListMealsResponse
import com.example.data.network.MealsApi
import com.example.data.repo.repoNetwork.MealsNetworkRepo
import retrofit2.Response

class MealsNetworkRepoImpl(private val mealsApi: MealsApi) : MealsNetworkRepo {

    override suspend fun getRandomMeal() = mealsApi.getRandomMeal()

    override suspend fun getMealById(mealId: String) = mealsApi.getMealById(mealId)

    override suspend fun getMealCategory(categoryName: String) =
        mealsApi.getMealCategory(categoryName)

    override suspend fun getAllMealsCategory() = mealsApi.getAllMealsCategory()

    override suspend fun searchMealByName(mealName: String): Response<ListMealsResponse> {
        return mealsApi.searchMealByName(mealName)
    }

}