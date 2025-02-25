package com.example.foodapp.view

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.example.data.model.ListMealsResponse
import com.example.data.model.Meal
import com.example.data.model.MealCategoryResponse
import com.example.foodapp.R
import com.example.foodapp.adapters.CategoryMealsAdapter
import com.example.foodapp.adapters.MealsAdapter
import com.example.foodapp.databinding.ActivitySearchMealBinding
import com.example.foodapp.utils.Resource
import com.example.foodapp.utils.hide
import com.example.foodapp.utils.show
import com.example.foodapp.utils.toast
import com.example.foodapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMealActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchMealBinding
    private lateinit var mealsAdapter: MealsAdapter

    // this dagger hilt get automatic instance from mealsViewModel
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) { // API 30 corresponds to Android 11 (R)
            // Code for API levels above 30
            findViewById<View>(R.id.view).visibility = View.VISIBLE
        } else {
            // Code for API 30 and below
            findViewById<View>(R.id.view).visibility = View.GONE
            window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)
        }

        setSupportActionBar(binding.toolbarSearchMeal)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbarSearchMeal.setNavigationOnClickListener {
            finish()
        }

        binding.lottieAnimationViewSearch.show()

        observeSearchMeal()

    }

    private fun observeSearchMeal() {
        homeViewModel.searchMealLiveData.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.lottieAnimationViewSearch.hide()
                    resource.data.let { listMealsResponse ->
                        prepareSearchMealRecyclerView(listMealsResponse)
                    }
                }

                is Resource.Failure -> {
                    binding.lottieAnimationViewSearch.hide()
                    resource.errorMessage?.let { message ->
                        toast("An Error Occurred: $message")
                    }
                }

                is Resource.Loading -> {
                    binding.lottieAnimationViewSearch.show()
                }
            }
        }
    }

    private fun prepareSearchMealRecyclerView(listMealsResponse: ListMealsResponse) {
        mealsAdapter = MealsAdapter()
        mealsAdapter.submitList(listMealsResponse.meals)
        binding.rvSearchMeal.adapter = mealsAdapter
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_meal, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_search_meal)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val meal = searchView.query.toString()
                return false
            }

            // Handle text change event
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText == "") {
                    mealsAdapter.submitList(arrayListOf())
                    binding.lottieAnimationViewSearch.show()
                } else {
                    homeViewModel.searchMeal(newText)
                }
                return false
            }
        })

        return true
    }


}