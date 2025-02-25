package com.example.domain.usecase.usecaseDb

import com.example.data.model.Category
import com.example.data.model.Meal
import com.example.data.model.MealCategory
import com.example.data.repo.repoDb.MealsDbRepo

class MealsDbUseCase(private val mealsDbRepo: MealsDbRepo) {

    suspend fun insertOrUpdateMeal(meal: Meal) {
        mealsDbRepo.insertOrUpdateMeal(meal)
    }

    suspend fun insertOrUpdateMealCategory(listMealCategory: List<MealCategory>) {
        if (listMealCategory.size != getMealsCategoryFromDb().size) {
            for (mealCategory in listMealCategory) {
                mealsDbRepo.insertOrUpdateMealCategory(mealCategory)
            }
        } else return
    }

    suspend fun insertOrUpdateCategories(listCategories: List<Category>) {
        if (listCategories.size != getAllCategoriesFromDb().size) {
            for (category in listCategories) {
                mealsDbRepo.insertOrUpdateCategories(category)
            }
        } else return
    }

    suspend fun getAllMeals() = mealsDbRepo.getAllMeals()

    suspend fun getMealsCategoryFromDb(): List<MealCategory> {
        return mealsDbRepo.getMealsCategoryFromDb()
    }

    suspend fun getAllCategoriesFromDb(): List<Category> {
        return mealsDbRepo.getAllCategoriesFromDb()
    }

    suspend fun deleteMeal(meal: Meal) {
        mealsDbRepo.deleteMeal(meal)
    }

    suspend fun searchMealByIdFromDb(mealId: String): Meal {
        return mealsDbRepo.searchMealByIdFromDb(mealId)
    }

}