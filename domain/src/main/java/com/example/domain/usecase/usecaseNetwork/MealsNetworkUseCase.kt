package com.example.domain.usecase.usecaseNetwork

import com.example.data.repo.repoNetwork.MealsNetworkRepo

class MealsNetworkUseCase(private val mealsNetworkRepo: MealsNetworkRepo) {

    suspend fun getRandomMeal() = mealsNetworkRepo.getRandomMeal()

    suspend fun getMealBbId(mealId: String) = mealsNetworkRepo.getMealById(mealId)


    suspend fun getMealCategory(categoryName: String) =
        mealsNetworkRepo.getMealCategory(categoryName)

    suspend fun getAllMealsCategory() = mealsNetworkRepo.getAllMealsCategory()

    suspend fun searchMealByName(mealName: String) = mealsNetworkRepo.searchMealByName(mealName)

}