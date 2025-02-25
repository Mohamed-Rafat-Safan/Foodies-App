package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.utils.Constant.CATEGORIES_TABLE_NAME

@Entity(tableName = CATEGORIES_TABLE_NAME)
data class Category(
    @PrimaryKey
    val idCategory: String,
    val strCategory: String,
    val strCategoryDescription: String,
    val strCategoryThumb: String
)