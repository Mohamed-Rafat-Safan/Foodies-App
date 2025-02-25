package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.model.MealCategory
import com.example.foodapp.databinding.MealItemBinding

class CategoryMealsAdapter :
    ListAdapter<MealCategory, CategoryMealsAdapter.CategoryMealsViewHolder>(CategoryMealsDiffCallback()) {

//    private lateinit var listener: OnItemClickListener

    // 3. Set the Listener (Setter Method)
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        this.listener = listener
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
        val itemBinding =
            MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryMealsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
        holder.bind(getItem(position))

//        holder.itemView.setOnClickListener {
//            listener.onItemClick(getItem(position))
//        }
    }


    class CategoryMealsViewHolder(private val itemBinding: MealItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(mealCategory: MealCategory) {
            itemBinding.tvMealCategoryName.text = mealCategory.strMeal
            Glide.with(itemBinding.root.context).load(mealCategory.strMealThumb)
                .into(itemBinding.ivMealCategory)
        }
    }

    class CategoryMealsDiffCallback : DiffUtil.ItemCallback<MealCategory>() {
        override fun areItemsTheSame(oldItem: MealCategory, newItem: MealCategory): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: MealCategory, newItem: MealCategory): Boolean {
            return oldItem == newItem
        }
    }


//    interface OnItemClickListener {
//        fun onItemClick(mealX: MealX)
//    }

}