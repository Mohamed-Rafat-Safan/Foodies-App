package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.model.Meal
import com.example.foodapp.databinding.MealItemBinding

class MealsAdapter:ListAdapter<Meal,MealsAdapter.FavoriteMealsViewHolder>(MealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMealsViewHolder {
        val itemBinding = MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteMealsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: FavoriteMealsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteMealsViewHolder(private val itemBinding: MealItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
            fun bind(meal: Meal){
                itemBinding.tvMealCategoryName.text = meal.strMeal
                Glide.with(itemBinding.root.context).load(meal.strMealThumb)
                    .into(itemBinding.ivMealCategory)
            }
        }

    private class MealDiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

}