package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.utils.Constant.MEAL_CATEGORY_TABLE_NAME


@Entity(tableName = MEAL_CATEGORY_TABLE_NAME)
data class MealCategory(
    @PrimaryKey
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
)
