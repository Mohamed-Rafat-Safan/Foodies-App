package com.example.foodapp.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.data.model.Meal
import com.example.foodapp.adapters.MealsAdapter
import com.example.foodapp.databinding.FragmentFavoritesBinding
import com.example.foodapp.utils.Resource
import com.example.foodapp.utils.hide
import com.example.foodapp.utils.show
import com.example.foodapp.utils.toast
import com.example.foodapp.viewmodel.DatabaseViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    // this dagger hilt get automatic instance from mealsViewModel
    private val databaseViewModel: DatabaseViewModel by viewModels()

    private lateinit var mealsAdapter: MealsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

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

        databaseViewModel.getFavoriteMealFromDb()
        observerFavoriteMeal()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                updateFavoriteMealRecyclerView(position)
            }
        }


        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavoriteMeals)

    }

    private fun observerFavoriteMeal() {
        databaseViewModel.favoriteMealDbLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.progressBarFavorite.hide()
                    resource.data.let { mealList ->
                        prepareFavoriteMealRecyclerView(mealList)
                    }
                }

                is Resource.Failure -> {
                    binding.progressBarFavorite.hide()
                    resource.errorMessage?.let { message ->
                        toast("An Error Occurred: $message")
                    }
                }

                is Resource.Loading -> {
                    binding.progressBarFavorite.show()
                }
            }
        }
    }


    private fun prepareFavoriteMealRecyclerView(mealList: List<Meal>) {
        mealsAdapter = MealsAdapter()
        mealsAdapter.submitList(mealList)
        binding.rvFavoriteMeals.adapter = mealsAdapter
    }


    private fun updateFavoriteMealRecyclerView(position: Int) {
        if (position != RecyclerView.NO_POSITION) {

            val mealToDelete = mealsAdapter.currentList[position]
            val currentList = mealsAdapter.currentList.toMutableList()
            currentList.removeAt(position)

            mealsAdapter.submitList(currentList)

            databaseViewModel.deleteFavoriteMealFromDb(mealToDelete)

            Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG).setAction(
                "Undo", {
                    databaseViewModel.insertMeal(mealToDelete)

                    val currentList = mealsAdapter.currentList.toMutableList()
                    currentList.add(position, mealToDelete)

                    mealsAdapter.submitList(currentList)
                }).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}