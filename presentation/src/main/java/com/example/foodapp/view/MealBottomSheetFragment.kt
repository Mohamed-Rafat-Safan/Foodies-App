package com.example.foodapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.data.model.Meal
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentMealBottomSheetBinding
import com.example.foodapp.utils.IsInternetAvailable
import com.example.foodapp.utils.Resource
import com.example.foodapp.utils.toast
import com.example.foodapp.viewmodel.MealDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentMealBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController

    // [by] to get value direct therefor don't (.value)
    private val args by navArgs<MealBottomSheetFragmentArgs>()

    private val mealDetailsViewModel: MealDetailsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMealBottomSheetBinding.inflate(inflater, container, false)
        mNavController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealId = args.mealId

        mealDetailsViewModel.getMealById(mealId)
        observerMealById()


        binding.bottomSheet.setOnClickListener {
            if (!IsInternetAvailable(requireContext())) {
                toast("No internet connection")
                return@setOnClickListener
            }
            val action =
                HomeFragmentDirections.actionHomeFragmentToMealDetailsActivity(mealId)
            mNavController.navigate(action)
        }
    }

    private fun observerMealById() {
        mealDetailsViewModel.mealByIdLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
//                    binding.progressBarHomeFrag.hide()
                    resource.data.let { listMealResponse ->
                        val meal = listMealResponse.meals[0]
                        updateUI(meal)
                    }
                }

                is Resource.Failure -> {
//                    binding.progressBarHomeFrag.hide()
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

    private fun updateUI(meal: Meal) {
        binding.apply {
            tvMealName.text = meal.strMeal
            tvMealCategory.text = meal.strCategory
            tvMealArea.text = meal.strArea
            tvInstructions.text = meal.strInstructions
        }
        Glide.with(this@MealBottomSheetFragment)
            .load(meal.strMealThumb)
            .into(binding.ivMeal)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}