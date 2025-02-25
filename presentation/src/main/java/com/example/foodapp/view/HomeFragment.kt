package com.example.foodapp.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.data.model.Category
import com.example.data.model.MealCategory
import com.example.data.model.MealsCategoriesResponse
import com.example.data.model.ListMealsResponse
import com.example.foodapp.adapters.CategoriesAdapter
import com.example.foodapp.adapters.PopularMealAdapter
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.utils.Constant.KEY_RANDOM_MEAL_IMAGE
import com.example.foodapp.utils.Constant.MY_PREFS
import com.example.foodapp.utils.IsInternetAvailable
import com.example.foodapp.utils.Resource
import com.example.foodapp.utils.hide
import com.example.foodapp.utils.show
import com.example.foodapp.utils.toast
import com.example.foodapp.viewmodel.DatabaseViewModel
import com.example.foodapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), PopularMealAdapter.OnItemClickListener,
    CategoriesAdapter.OnItemClickListenerAllCategory, PopularMealAdapter.OnItemLongClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController
    private var randomMeal: ListMealsResponse? = null

    // this dagger hilt get automatic instance from mealsViewModel
    private val homeViewModel: HomeViewModel by viewModels()
    private val databaseViewModel: DatabaseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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

        binding.ivSearchMeal.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchMealActivity()
            mNavController.navigate(action)
        }


        // check internet connection
        if (IsInternetAvailable(requireContext())) {
            homeViewModel.getRandomMeal()
            observerRandomMeal()

            homeViewModel.getMealsCategory("Seafood")
            observerMealCategory()

            homeViewModel.getAllMealsCategory()
            observerAllMealsCategory()
        } else {
            // get data from shared preferences and room database if there is no internet connection
            prepareRandomMealFromPrefs()
            preparePopularMealCategoryFromDb()
            prepareAllCategoriesFromDb()
        }


        binding.cardViewRandomMeal.setOnClickListener {
            if (!IsInternetAvailable(requireContext())) {
                toast("No internet connection")
                return@setOnClickListener
            }
            if (randomMeal != null) {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToMealDetailsActivity(randomMeal!!.meals[0].idMeal)
                mNavController.navigate(action)
            }
        }


    }

    override fun onResume() {
        super.onResume()
        // to hide the bottom sheet if it is visible
        dismissMealBottomSheet()
    }

    private fun dismissMealBottomSheet() {
        val mealBottomSheetFragment =
            childFragmentManager.findFragmentByTag("MealBottomSheet") as? MealBottomSheetFragment
        mealBottomSheetFragment?.dismiss()
    }

    private fun observerRandomMeal() {
        homeViewModel.randomMealLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.progressBarHomeFrag.hide()
                    binding.lottieAnimationViewMeal.hide()
                    resource.data.let { listMealsResponse ->
                        randomMeal = listMealsResponse
                        Glide.with(this@HomeFragment)
                            .load(listMealsResponse.meals[0].strMealThumb)
                            .into(binding.ivRandomMeal)

                        // save image in shared preferences
                        saveMealImageInSharedPrefs(listMealsResponse.meals[0].strMealThumb)
                    }
                }

                is Resource.Failure -> {
                    binding.progressBarHomeFrag.hide()
                    resource.errorMessage?.let { message ->
                        toast("An Error Occurred: $message")
                    }
                }

                is Resource.Loading -> {
                    binding.progressBarHomeFrag.show()
                    binding.lottieAnimationViewMeal.show()
                }
            }
        }
    }


    private fun observerMealCategory() {
        homeViewModel.mealsCategoryLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data.let { mealCategoryResponse ->
                        preparePopularMealRecyclerView(mealCategoryResponse.meals)

                        // insert meal category in database
                        databaseViewModel.insertPopularMealCategory(mealCategoryResponse.meals)
                    }
                }

                is Resource.Failure -> {
                    resource.errorMessage?.let { message ->
                        toast("An Error Occurred: $message")
                    }
                }

                is Resource.Loading -> {
//                    binding.progressBarHomeFrag.show()
                }
            }
        }
    }

    private fun observerAllMealsCategory() {
        homeViewModel.allMealCategoryLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data.let { mealCategoriesResponse ->
                        prepareAllCategoriesRecyclerView(mealCategoriesResponse.categories)

                        // insert all categories in database
                        databaseViewModel.insertCategories(mealCategoriesResponse.categories)
                    }
                }

                is Resource.Failure -> {
                    resource.errorMessage?.let { message ->
                        toast("An Error Occurred: $message")
                    }
                }

                is Resource.Loading -> {
//                    binding.progressBarHomeFrag.show()
                }
            }
        }
    }


    private fun prepareRandomMealFromPrefs() {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE)
        val mealImageUrl = sharedPreferences.getString(KEY_RANDOM_MEAL_IMAGE, null)

        if (mealImageUrl != null) {
            Glide.with(this)
                .load(mealImageUrl)
                .into(binding.ivRandomMeal)
        } else {
            binding.lottieAnimationViewMeal.show()
        }
    }

    private fun preparePopularMealCategoryFromDb() {
        databaseViewModel.getPopularMealsCategoryFromDb()
        databaseViewModel.popularMealsCategoryDbLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data.let { listMealCategory ->
                        preparePopularMealRecyclerView(listMealCategory)
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

    private fun preparePopularMealRecyclerView(listMealCategory: List<MealCategory>) {
        val popularMealAdapter = PopularMealAdapter()
        popularMealAdapter.submitList(listMealCategory)
        binding.rvPopularItem.adapter = popularMealAdapter

        // initialize of OnItemClickListener interface
        popularMealAdapter.setOnItemClickListener(this)

        // initialize of OnItemLongClickListener interface
        popularMealAdapter.setOnItemLongClickListener(this)
    }

    private fun prepareAllCategoriesRecyclerView(listCategories: List<Category>) {
        val categoriesAdapter = CategoriesAdapter()
        categoriesAdapter.submitList(listCategories)
        binding.rvCategories.adapter = categoriesAdapter

        // initialize of OnItemClickListener interface
        categoriesAdapter.setOnItemClickListenerAllCategory(this)
    }


    // save image in shared preferences
    private fun saveMealImageInSharedPrefs(mealImageUrl: String) {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_RANDOM_MEAL_IMAGE, mealImageUrl)
        editor.apply() // or editor.commit()
    }


    // if click on item in popular meals
    override fun onItemClick(mealCategory: MealCategory) {
        if (!IsInternetAvailable(requireContext())) {
            toast("No internet connection")
            return
        }
        if (randomMeal != null) {
            val action =
                HomeFragmentDirections.actionHomeFragmentToMealDetailsActivity(mealCategory.idMeal)
            mNavController.navigate(action)
        }
    }

    override fun onItemClickAllCategory(category: Category) {
        if (!IsInternetAvailable(requireContext())) {
            toast("No internet connection")
            return
        }
        val action =
            HomeFragmentDirections.actionHomeFragmentToMealsCategoryActivity(category.strCategory)
        mNavController.navigate(action)
    }

    override fun onItemLongClick(meal: MealCategory) {
        // Create a bundle to pass data to the bottom sheet
        val bundle = Bundle().apply {
            putString("mealId", meal.idMeal)
        }

        // Create the MealBottomSheetFragment and set the arguments
        val mealBottomSheetFragment = MealBottomSheetFragment()
        mealBottomSheetFragment.arguments = bundle

        // Show the bottom sheet
        mealBottomSheetFragment.show(childFragmentManager, "MealBottomSheet")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}