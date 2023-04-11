package com.ezatpanah.mvvm_themealdb_api.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.ezatpanah.mvvm_themealdb_api.R
import com.ezatpanah.mvvm_themealdb_api.adapter.CategoriesAdapter
import com.ezatpanah.mvvm_themealdb_api.adapter.FoodsAdapter
import com.ezatpanah.mvvm_themealdb_api.databinding.FragmentHomeBinding
import com.ezatpanah.mvvm_themealdb_api.utils.*
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

    @Inject
    lateinit var connection: CheckConnection

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    enum class PageState { EMPTY, NETWORK, NONE }


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
                categoriesAdapter.setOnItemClickListener {
                    viewModel.getFoodByCategory(it.strCategory.toString())
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
                            if (it.data!!.meals != null) {
                                if (it.data.meals!!.isNotEmpty()) {
                                    checkConnectionOrEmpty(false, PageState.NONE)
                                    foodsAdapter.setData(it.data.meals)
                                    foodsList.setupRecyclerView(
                                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false),
                                        foodsAdapter
                                    )
                                }
                            } else {
                                checkConnectionOrEmpty(true, PageState.EMPTY)
                            }
                        }
                        DataStatus.Status.ERROR -> {
                            homeFoodsLoading.isVisible(false, foodsList)
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                foodsAdapter.setOnItemClickListener {
                    val direction = HomeFragmentDirections.actionHomeToDetail(it.idMeal!!.toInt())
                    findNavController().navigate(direction)
                }

                //Search
                searchEdt.addTextChangedListener {
                    if (it.toString().length > 2) {
                        viewModel.getFoodBySearch(it.toString())
                    }
                }

                //Internet
                connection.observe(viewLifecycleOwner) {
                    if (it) {
                        checkConnectionOrEmpty(false, PageState.NONE)
                    } else {
                        checkConnectionOrEmpty(true, PageState.NETWORK)
                    }
                }
            }
        }
    }

    private fun checkConnectionOrEmpty(isShownError: Boolean, state: PageState) {
        binding?.apply {
            if (isShownError) {
                homeDisLay.isVisible(true, homeContent)
                when (state) {
                    PageState.EMPTY -> {
                        homeContent.visibility = View.GONE
                        homeDisLay.visibility = View.VISIBLE
                        disconnectLay.imgDisconnect.setAnimation(R.raw.empty)
                        disconnectLay.imgDisconnect.playAnimation()
                    }
                    PageState.NETWORK -> {
                        homeContent.visibility = View.GONE
                        homeDisLay.visibility = View.VISIBLE
                        disconnectLay.imgDisconnect.setAnimation(R.raw.nointernet)
                        disconnectLay.imgDisconnect.playAnimation()
                    }
                    else -> {}
                }
            } else {
                homeDisLay.isVisible(false, homeContent)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}