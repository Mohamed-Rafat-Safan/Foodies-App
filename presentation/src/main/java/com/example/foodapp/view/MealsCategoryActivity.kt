package com.example.foodapp.view

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.example.data.model.MealCategoryResponse
import com.example.foodapp.R
import com.example.foodapp.adapters.CategoryMealsAdapter
import com.example.foodapp.databinding.ActivityMealsCategoryBinding
import com.example.foodapp.utils.Resource
import com.example.foodapp.utils.hide
import com.example.foodapp.utils.show
import com.example.foodapp.utils.toast
import com.example.foodapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealsCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealsCategoryBinding

    private val homeViewModel: HomeViewModel by viewModels()

    private val args: MealsCategoryActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealsCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) { // API 30 corresponds to Android 11 (R)
            // Code for API levels above 30
            findViewById<View>(R.id.view).visibility = View.VISIBLE
        } else {
            // Code for API 30 and below
            findViewById<View>(R.id.view).visibility = View.GONE
            window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)
        }


        val mealCategory = args.categoryName

        binding.tvCategoryName.text = mealCategory

        binding.ivBack.setOnClickListener {
            finish()
        }


        homeViewModel.getMealsCategory(mealCategory)
        observerMealCategory()

    }

    private fun observerMealCategory() {
        homeViewModel.mealsCategoryLiveData.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.progressBarMealCategory.hide()
                    resource.data.let { mealCategoryResponse ->
                        prepareMealsCategoryRecyclerView(mealCategoryResponse)
                    }
                }

                is Resource.Failure -> {
                    binding.progressBarMealCategory.hide()
                    resource.errorMessage?.let { message ->
                        toast("An Error Occurred: $message")
                    }
                }

                is Resource.Loading -> {
                    binding.progressBarMealCategory.show()
                }
            }
        }
    }



    private fun prepareMealsCategoryRecyclerView(mealCategoryResponse: MealCategoryResponse) {
        val categoryMealsAdapter = CategoryMealsAdapter()
        categoryMealsAdapter.submitList(mealCategoryResponse.meals)
        binding.rvAllCategory.adapter = categoryMealsAdapter
    }


}