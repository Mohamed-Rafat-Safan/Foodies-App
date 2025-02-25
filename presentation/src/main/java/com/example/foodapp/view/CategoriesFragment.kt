package com.example.foodapp.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.data.model.Category
import com.example.data.model.MealsCategoriesResponse
import com.example.foodapp.R
import com.example.foodapp.adapters.CategoriesAdapter
import com.example.foodapp.databinding.FragmentCategoriesBinding
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.utils.IsInternetAvailable
import com.example.foodapp.utils.Resource
import com.example.foodapp.utils.hide
import com.example.foodapp.utils.show
import com.example.foodapp.utils.toast
import com.example.foodapp.viewmodel.DatabaseViewModel
import com.example.foodapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment(), CategoriesAdapter.OnItemClickListenerAllCategory {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController

    private val homeViewModel: HomeViewModel by viewModels()
    private val databaseViewModel: DatabaseViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        mNavController = findNavController()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) { // API 30 corresponds to Android 11 (R)
            // Code for API levels above 30
            binding.view.visibility = View.VISIBLE
        } else {
            // Code for API 30 and below
            binding.view.visibility = View.GONE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // check internet connection
        if (IsInternetAvailable(requireContext())) {
            homeViewModel.getAllMealsCategory()
            observerAllMealsCategory()
        }else{
            // get data from room database if there is no internet connection
            prepareAllCategoriesFromDb()
        }


    }

    private fun observerAllMealsCategory() {
        homeViewModel.allMealCategoryLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.progressBarCategories.hide()
                    resource.data.let { mealsCategoriesResponse ->
                        prepareAllCategoriesRecyclerView(mealsCategoriesResponse.categories)

                        // insert all categories in database
                        databaseViewModel.insertCategories(mealsCategoriesResponse.categories)
                    }
                }

                is Resource.Failure -> {
                    binding.progressBarCategories.hide()
                    resource.errorMessage?.let { message ->
                        toast("An Error Occurred: $message")
                    }
                }

                is Resource.Loading -> {
                    binding.progressBarCategories.show()
                }
            }
        }
    }


    private fun prepareAllCategoriesFromDb() {
        databaseViewModel.getAllCategoriesFromDb()
        databaseViewModel.allCategoriesDbLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data.let { listCategories ->
                        prepareAllCategoriesRecyclerView(listCategories)
                    }
                }

                is Resource.Failure -> {
                    resource.errorMessage?.let { message ->
                        toast("An Error Occurred: $message")
                    }
                }

                is Resource.Loading -> {}
            }
        }
    }


    private fun prepareAllCategoriesRecyclerView(listCategories: List<Category>) {
        val categoriesAdapter = CategoriesAdapter()
        categoriesAdapter.submitList(listCategories)
        binding.rvMealsCategories.adapter = categoriesAdapter

        // initialize of OnItemClickListener interface
        categoriesAdapter.setOnItemClickListenerAllCategory(this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClickAllCategory(category: Category) {
        val action =
            CategoriesFragmentDirections.actionCategoriesFragmentToMealsCategoryActivity(category.strCategory)
        mNavController.navigate(action)
    }
}