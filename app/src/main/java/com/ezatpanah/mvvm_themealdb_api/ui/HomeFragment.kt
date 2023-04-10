package com.ezatpanah.mvvm_themealdb_api.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.ezatpanah.mvvm_themealdb_api.R
import com.ezatpanah.mvvm_themealdb_api.adapter.CategoriesAdapter
import com.ezatpanah.mvvm_themealdb_api.adapter.FoodsAdapter
import com.ezatpanah.mvvm_themealdb_api.databinding.FragmentHomeBinding
import com.ezatpanah.mvvm_themealdb_api.utils.DataStatus
import com.ezatpanah.mvvm_themealdb_api.utils.isVisible
import com.ezatpanah.mvvm_themealdb_api.utils.setupListWithAdapter
import com.ezatpanah.mvvm_themealdb_api.utils.setupRecyclerView
import com.ezatpanah.mvvm_themealdb_api.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter

    @Inject
    lateinit var foodsAdapter: FoodsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleScope.launch {
                //Header Picture
                viewModel.getRandomFood()
                viewModel.randomFood.observe(viewLifecycleOwner) {
                    it[0].let { meal ->
                        headerImg.load(meal.strMealThumb) {
                            crossfade(true)
                            crossfade(500)
                        }
                    }
                }
                //Filters
                viewModel.loadFilterList()
                viewModel.filtersListData.observe(viewLifecycleOwner) {
                    filterSpinner.setupListWithAdapter(it) { letter ->
                        Toast.makeText(requireContext(), letter, Toast.LENGTH_SHORT).show()
                    }
                }
                //Categories
                viewModel.getCategoriesList()
                viewModel.categoriesList.observe(viewLifecycleOwner) {
                    when (it.status) {
                        DataStatus.Status.LOADING -> {
                            homeCategoryLoading.isVisible(true, categoryList)
                        }
                        DataStatus.Status.SUCCESS -> {
                            homeCategoryLoading.isVisible(false, categoryList)
                            categoriesAdapter.setData(it.data!!.categories)
                            categoryList.setupRecyclerView(
                                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                                categoriesAdapter
                            )
                        }
                        DataStatus.Status.ERROR -> {
                            homeCategoryLoading.isVisible(false, categoryList)
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                //Foods
                viewModel.getFoodsList("A")
                viewModel.foodList.observe(viewLifecycleOwner) {
                    when (it.status) {
                        DataStatus.Status.LOADING -> {
                            homeFoodsLoading.isVisible(true, foodsList)
                        }
                        DataStatus.Status.SUCCESS -> {
                            homeFoodsLoading.isVisible(false, foodsList)
                            foodsAdapter.setData(it.data!!.meals!!)
                            foodsList.setupRecyclerView(
                                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false),
                                foodsAdapter
                            )
                        }
                        DataStatus.Status.ERROR -> {
                            homeFoodsLoading.isVisible(false, foodsList)
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}