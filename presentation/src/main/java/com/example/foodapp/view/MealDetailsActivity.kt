package com.example.foodapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.MediaController
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.data.model.Meal
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityMealDetailsBinding
import com.example.foodapp.databinding.CollapsingToolbarLayoutBinding
import com.example.foodapp.utils.Resource
import com.example.foodapp.utils.hide
import com.example.foodapp.utils.show
import com.example.foodapp.utils.toast
import com.example.foodapp.viewmodel.DatabaseViewModel
import com.example.foodapp.viewmodel.HomeViewModel
import com.example.foodapp.viewmodel.MealDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MealDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealDetailsBinding
    private lateinit var customBinding: CollapsingToolbarLayoutBinding

    // [by] to get value direct therefor don't (.value)
    private val args by navArgs<MealDetailsActivityArgs>()

    // this dagger hilt get automatic instance from mealsViewModel
    private val mealDetailsViewModel: MealDetailsViewModel by viewModels()
    private val databaseViewModel: DatabaseViewModel by viewModels()

    private lateinit var linkVideoYoutube: String
    private lateinit var currentMeal: Meal


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMealDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Accessing the separate layout using ViewBinding
        customBinding = CollapsingToolbarLayoutBinding.inflate(layoutInflater)
        binding.main.addView(customBinding.root)  // Add it to your main layout if needed


        // get meal by id from api
        mealDetailsViewModel.getMealById(args.mealId)
        observeMealByIdLiveData()


        checkSavedMealInDb(args.mealId)


        binding.fabFavorite.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                databaseViewModel.insertMeal(currentMeal)
                withContext(Dispatchers.Main) {
                    toast("Meal Saved")
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite_filled)
                }
            }
        }

        binding.fabVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkVideoYoutube))
            startActivity(intent)
        }
    }


    private fun checkSavedMealInDb(mealId: String) {
        databaseViewModel.searchMealById(mealId)
        databaseViewModel.searchMealByIdLiveData.observe(this) { meal ->
            if (meal != null) {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }
    }


    private fun observeMealByIdLiveData() {
        mealDetailsViewModel.mealByIdLiveData.observe(this, { resource ->
            when (resource) {
                is Resource.Success -> {
                    response()
                    resource.data.let { listMealsResponse ->
                        currentMeal = listMealsResponse.meals[0]
                        linkVideoYoutube = listMealsResponse.meals[0].strYoutube
                        updateDataInUi(currentMeal)
                    }
                }

                is Resource.Failure -> {
                    response()
                    resource.errorMessage?.let { message ->
                        toast("An Error Occurred: $message")
                    }
                }

                is Resource.Loading -> {
                    loading()
                }
            }
        })
    }


    private fun updateDataInUi(meal: Meal) {
        Glide.with(this)
            .load(meal.strMealThumb)
            .into(customBinding.ivRandomMealDetails)

        binding.apply {
            tvMealName.text = meal.strMeal
            tvMealCategory.text = meal.strCategory
            tvMealArea.text = meal.strArea
            tvInstructions.text = meal.strInstructions
        }
    }


    private fun loading() {
        binding.apply {
            tvMealName.hide()
            linearLayoutCategory.hide()
            linearLayoutArea.hide()
            tvInstructionTitle.hide()
            tvInstructions.hide()
            fabFavorite.hide()
            fabVideo.hide()
            progressBarMealDetails.show()
        }
    }

    private fun response() {
        binding.apply {
            progressBarMealDetails.hide()
            tvMealName.show()
            linearLayoutCategory.show()
            linearLayoutArea.show()
            tvInstructionTitle.show()
            tvInstructions.show()
            fabFavorite.show()
            fabVideo.show()
        }
    }
}