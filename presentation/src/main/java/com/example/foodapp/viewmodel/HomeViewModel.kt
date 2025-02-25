package com.example.foodapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.data.model.MealsCategoriesResponse
import com.example.data.model.ListMealsResponse
import com.example.data.model.MealCategoryResponse
import com.example.domain.usecase.usecaseNetwork.MealsNetworkUseCase
import com.example.foodapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val app: Application,
    private val mealsNetworkUseCase: MealsNetworkUseCase,
) : AndroidViewModel(app) {

    // this live data for network
    private val _randomMealLiveData = MutableLiveData<Resource<ListMealsResponse>>()
    val randomMealLiveData: LiveData<Resource<ListMealsResponse>> get() = _randomMealLiveData

    private val _allMealCategoryLiveData = MutableLiveData<Resource<MealsCategoriesResponse>>()
    val allMealCategoryLiveData: LiveData<Resource<MealsCategoriesResponse>> get() = _allMealCategoryLiveData

    private val _mealsCategoryLiveData = MutableLiveData<Resource<MealCategoryResponse>>()
    val mealsCategoryLiveData: LiveData<Resource<MealCategoryResponse>> get() = _mealsCategoryLiveData

    private val _searchMealLiveData = MutableLiveData<Resource<ListMealsResponse>>()
    val searchMealLiveData: LiveData<Resource<ListMealsResponse>> get() = _searchMealLiveData


    fun getRandomMeal() = viewModelScope.launch {
        _randomMealLiveData.value = Resource.Loading
        try {
            val response = mealsNetworkUseCase.getRandomMeal()
            _randomMealLiveData.value = handelMealsResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _randomMealLiveData.value = Resource.Failure("Network failure")
                else -> _randomMealLiveData.value = Resource.Failure("Conversion Error")
            }
        }
    }

    fun getAllMealsCategory() = viewModelScope.launch {
        _allMealCategoryLiveData.value = Resource.Loading
        try {
            val response = mealsNetworkUseCase.getAllMealsCategory()
            _allMealCategoryLiveData.value = handelAllMealsCategoryResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _allMealCategoryLiveData.value =
                    Resource.Failure("Network failure")

                else -> _allMealCategoryLiveData.value = Resource.Failure("Conversion Error")
            }
        }

    }

    fun getMealsCategory(categoryName: String) = viewModelScope.launch {
        _mealsCategoryLiveData.value = Resource.Loading
        try {
            val response = mealsNetworkUseCase.getMealCategory(categoryName)
            _mealsCategoryLiveData.value = handelMealsCategoryResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _mealsCategoryLiveData.value = Resource.Failure("Network failure")
                else -> _mealsCategoryLiveData.value = Resource.Failure("Conversion Error")
            }
        }

    }

    fun searchMeal(mealName: String) = viewModelScope.launch {
        _searchMealLiveData.value = Resource.Loading
        try {
            val response = mealsNetworkUseCase.searchMealByName(mealName)
            _searchMealLiveData.value = handelMealsResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _searchMealLiveData.value = Resource.Failure("Network failure")
                else -> _searchMealLiveData.value = Resource.Failure("Conversion Error")
            }
        }
    }

    private fun handelMealsResponse(response: Response<ListMealsResponse>): Resource<ListMealsResponse> {
        // if data return and is good
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        // if data not return
        return Resource.Failure(response.message())
    }

    private fun handelAllMealsCategoryResponse(response: Response<MealsCategoriesResponse>): Resource<MealsCategoriesResponse> {
        // if data return and is good
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        // if data not return
        return Resource.Failure(response.message())
    }

    private fun handelMealsCategoryResponse(response: Response<MealCategoryResponse>): Resource<MealCategoryResponse> {
        // if data return and is good
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        // if data not return
        return Resource.Failure(response.message())
    }

}
