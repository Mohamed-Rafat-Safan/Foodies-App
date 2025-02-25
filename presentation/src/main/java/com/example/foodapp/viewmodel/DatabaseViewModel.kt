package com.example.foodapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.data.model.Category
import com.example.data.model.Meal
import com.example.data.model.MealCategory
import com.example.domain.usecase.usecaseDb.MealsDbUseCase
import com.example.foodapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val app: Application,
    private val mealsDbUseCase: MealsDbUseCase,
) : AndroidViewModel(app) {

    private val _favoriteMealDbLiveData = MutableLiveData<Resource<List<Meal>>>()
    val favoriteMealDbLiveData: LiveData<Resource<List<Meal>>> get() = _favoriteMealDbLiveData

    private val _popularMealsCategoryDbLiveData = MutableLiveData<Resource<List<MealCategory>>>()
    val popularMealsCategoryDbLiveData: LiveData<Resource<List<MealCategory>>> get() = _popularMealsCategoryDbLiveData

    private val _allCategoriesDbLiveData = MutableLiveData<Resource<List<Category>>>()
    val allCategoriesDbLiveData: LiveData<Resource<List<Category>>> get() = _allCategoriesDbLiveData

    private val _searchMealByIdLiveData = MutableLiveData<Meal>()
    val searchMealByIdLiveData: LiveData<Meal> get() = _searchMealByIdLiveData


    fun insertMeal(meal: Meal) = viewModelScope.launch {
        mealsDbUseCase.insertOrUpdateMeal(meal)
    }

    fun insertPopularMealCategory(listMealCategory: List<MealCategory>) = viewModelScope.launch {
        mealsDbUseCase.insertOrUpdateMealCategory(listMealCategory)
    }

    fun insertCategories(listCategories: List<Category>) = viewModelScope.launch {
        mealsDbUseCase.insertOrUpdateCategories(listCategories)
    }


    fun getFavoriteMealFromDb() = viewModelScope.launch {
        _favoriteMealDbLiveData.value = Resource.Loading
        try {
            val favoriteMeal = mealsDbUseCase.getAllMeals()
            _favoriteMealDbLiveData.value = Resource.Success(favoriteMeal)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _favoriteMealDbLiveData.value =
                    Resource.Failure("Connecting to database failure")

                else -> _favoriteMealDbLiveData.value = Resource.Failure("Conversion Error")
            }
        }
    }

    fun getPopularMealsCategoryFromDb() = viewModelScope.launch {
        _popularMealsCategoryDbLiveData.value = Resource.Loading
        try {
            val popularMealsCategory = mealsDbUseCase.getMealsCategoryFromDb()
            _popularMealsCategoryDbLiveData.value = Resource.Success(popularMealsCategory)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _popularMealsCategoryDbLiveData.value =
                    Resource.Failure("Connecting to database failure")

                else -> _popularMealsCategoryDbLiveData.value = Resource.Failure("Conversion Error")
            }
        }
    }

    fun getAllCategoriesFromDb() = viewModelScope.launch {
        _allCategoriesDbLiveData.value = Resource.Loading
        try {
            val allCategories = mealsDbUseCase.getAllCategoriesFromDb()
            _allCategoriesDbLiveData.value = Resource.Success(allCategories)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _allCategoriesDbLiveData.value =
                    Resource.Failure("Connecting to database failure")

                else -> _allCategoriesDbLiveData.value = Resource.Failure("Conversion Error")
            }
        }
    }


    fun deleteFavoriteMealFromDb(meal: Meal) = viewModelScope.launch {
        mealsDbUseCase.deleteMeal(meal)
    }


    fun searchMealById(mealId: String) = viewModelScope.launch {
        val meal = mealsDbUseCase.searchMealByIdFromDb(mealId)
        _searchMealByIdLiveData.value = meal
    }
}
