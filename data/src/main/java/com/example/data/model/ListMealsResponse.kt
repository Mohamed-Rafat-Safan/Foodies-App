package com.example.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListMealsResponse(
    val meals: List<Meal>
): Parcelable