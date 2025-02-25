package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.model.MealCategory
import com.example.foodapp.databinding.PopularItemBinding

class PopularMealAdapter :
    ListAdapter<MealCategory, PopularMealAdapter.PopularMealViewHolder>(MealDiffCallback()) {
    private lateinit var listener: OnItemClickListener
    private lateinit var longClickListener: OnItemLongClickListener

    // 3. Set the Listener (Setter Method)
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.longClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        val itemBinding =
            PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularMealViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.itemView.setOnClickListener {
            listener.onItemClick(getItem(position))
        }

        // 4. Detect the Long Click
        holder.itemView.setOnLongClickListener {
            // 5. Invoke the Long Click Listener
            longClickListener.onItemLongClick(getItem(position))
            true // Consume the long click event
        }
    }


    class PopularMealViewHolder(private val itemBinding: PopularItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(mealCategory: MealCategory) {
            Glide.with(itemBinding.root.context).load(mealCategory.strMealThumb)
                .into(itemBinding.ivPopularItem)
        }
    }

    private class MealDiffCallback : DiffUtil.ItemCallback<MealCategory>() {
        override fun areItemsTheSame(oldItem: MealCategory, newItem: MealCategory): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: MealCategory, newItem: MealCategory): Boolean {
            return oldItem == newItem
        }
    }


    interface OnItemClickListener {
        fun onItemClick(mealCategory: MealCategory)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(meal: MealCategory)
    }

}