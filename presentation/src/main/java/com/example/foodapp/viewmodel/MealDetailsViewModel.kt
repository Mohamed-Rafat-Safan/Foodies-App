package com.example.foodapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.data.model.ListMealsResponse
import com.example.data.model.Meal
import com.example.domain.usecase.usecaseDb.MealsDbUseCase
import com.example.domain.usecase.usecaseNetwork.MealsNetworkUseCase
import com.example.foodapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MealDetailsViewModel @Inject constructor(
    private val app: Application,
    private val mealsNetworkUseCase: MealsNetworkUseCase,
) : AndroidViewModel(app) {

    private val _mealByIdLiveData = MutableLiveData<Resource<ListMealsResponse>>()
    val mealByIdLiveData: LiveData<Resource<ListMealsResponse>> get() = _mealByIdLiveData

    fun getMealById(mealId: String) = viewModelScope.launch {
        _mealByIdLiveData.value = Resource.Loading
        try {
            val response = mealsNetworkUseCase.getMealBbId(mealId)
            _mealByIdLiveData.value = handelMealByIdResponse(response)

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _mealByIdLiveData.value = Resource.Failure("Network failure")
                else -> _mealByIdLiveData.value = Resource.Failure("Conversion Error")
            }
        }

    }

    private fun handelMealByIdResponse(response: Response<ListMealsResponse>): Resource<ListMealsResponse> {
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