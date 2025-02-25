package com.example.domain.repo.repoDb

import com.example.data.db.MealsDao
import com.example.data.model.Category
import com.example.data.model.Meal
import com.example.data.model.MealCategory
import com.example.data.repo.repoDb.MealsDbRepo

class MealsDbRepoImpl(private val mealsDao: MealsDao) : MealsDbRepo {

    override suspend fun insertOrUpdateMeal(meal: Meal) {
        mealsDao.insertOrUpdateMeal(meal)
    }

    override suspend fun insertOrUpdateMealCategory(mealCategory: MealCategory) {
        mealsDao.insertOrUpdateMealCategory(mealCategory)
    }

    override suspend fun insertOrUpdateCategories(category: Category) {
        mealsDao.insertOrUpdateCategories(category)
    }

    override suspend fun getAllMeals() = mealsDao.getAllMeals()

    override suspend fun getMealsCategoryFromDb(): List<MealCategory> {
        return mealsDao.getMealsCategoryFromDb()
    }

    override suspend fun getAllCategoriesFromDb(): List<Category> {
        return mealsDao.getAllCategoriesFromDb()
    }

    override suspend fun deleteMeal(meal: Meal) {
        mealsDao.deleteMeal(meal)
    }

    override suspend fun searchMealByIdFromDb(mealId: String): Meal {
        return mealsDao.searchMealByIdFromDb(mealId)
    }

}