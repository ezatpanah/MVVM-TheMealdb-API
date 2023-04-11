package com.ezatpanah.mvvm_themealdb_api.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.ezatpanah.mvvm_themealdb_api.R
import com.ezatpanah.mvvm_themealdb_api.databinding.FragmentDetailsBinding
import com.ezatpanah.mvvm_themealdb_api.db.FoodEntity
import com.ezatpanah.mvvm_themealdb_api.utils.CheckConnection
import com.ezatpanah.mvvm_themealdb_api.utils.Constant.VIDEO_ID
import com.ezatpanah.mvvm_themealdb_api.utils.DataStatus
import com.ezatpanah.mvvm_themealdb_api.utils.isVisible
import com.ezatpanah.mvvm_themealdb_api.viewmodel.DetailViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var connection: CheckConnection

    @Inject
    lateinit var entity: FoodEntity

    //Other
    private val args: DetailsFragmentArgs by navArgs()
    private var foodID = 0
    private val viewModel: DetailViewModel by viewModels()
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //Get data
            foodID = args.foodId
            //back
            detailBack.setOnClickListener { findNavController().navigateUp() }
            //Call api
            viewModel.loadFoodDetail(foodID)
            viewModel.foodDetailData.observe(viewLifecycleOwner) {
                when (it.status) {
                    DataStatus.Status.LOADING -> {
                        detailLoading.isVisible(true, detailContentLay)
                    }
                    DataStatus.Status.SUCCESS -> {
                        detailLoading.isVisible(false, detailContentLay)
                        //Set data
                        it.data?.meals?.get(0)?.let { itMeal ->
                            //Entity
                            entity.id = itMeal.idMeal!!.toInt()
                            entity.title = itMeal.strMeal.toString()
                            entity.img = itMeal.strMealThumb.toString()
                            //Set data
                            foodCoverImg.load(itMeal.strMealThumb) {
                                crossfade(true)
                                crossfade(500)
                            }
                            foodCategoryTxt.text = itMeal.strCategory
                            foodAreaTxt.text = itMeal.strArea
                            foodTitleTxt.text = itMeal.strMeal
                            foodDescTxt.text = itMeal.strInstructions
                            //Play
                            if (itMeal.strYoutube != null) {
                                foodPlayImg.visibility = View.VISIBLE
                                foodPlayImg.setOnClickListener {
                                    val videoId = itMeal.strYoutube.split("=")[1]
                                    Intent(requireContext(), PlayerActivity::class.java).also {
                                        it.putExtra(VIDEO_ID, videoId)
                                        startActivity(it)
                                    }
                                }
                            } else {
                                foodPlayImg.visibility = View.GONE
                            }
                            //Source
                            if (itMeal.strSource != null) {
                                foodSourceImg.visibility = View.VISIBLE
                                foodSourceImg.setOnClickListener {
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(itMeal.strSource)))
                                }
                            } else {
                                foodSourceImg.visibility = View.GONE
                            }
                        }
                        //Json Array
                        val jsonData = JSONObject(Gson().toJson(it.data))
                        val meals = jsonData.getJSONArray("meals")
                        val meal = meals.getJSONObject(0)
                        //Ingredient
                        for (i in 1..15) {
                            val ingredient = meal.getString("strIngredient$i")
                            if (ingredient.isNullOrEmpty().not()) {
                                ingredientsTxt.append("$ingredient\n")
                            }
                        }
                        //Measure
                        for (i in 1..15) {
                            val measure = meal.getString("strMeasure$i")
                            if (measure.isNullOrEmpty().not()) {
                                measureTxt.append("$measure\n")
                            }
                        }
                    }
                    DataStatus.Status.ERROR -> {
                        detailLoading.isVisible(false, detailContentLay)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            //Favorite
            viewModel.existsFood(foodID)
            viewModel.isFavoriteData.observe(viewLifecycleOwner) {
                isFavorite = it
                if (it) {
                    detailFav.setColorFilter(ContextCompat.getColor(requireContext(), R.color.tartOrange))
                } else {
                    detailFav.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
                }
            }
            //Save / Delete
            detailFav.setOnClickListener {
                if (isFavorite) {
                    viewModel.deleteFood(entity)
                } else
                    viewModel.saveFood(entity)
            }
        }
        //Internet
        connection.observe(viewLifecycleOwner) {
            if (it) {
                checkConnectionOrEmpty(false, HomeFragment.PageState.NONE)
            } else {
                checkConnectionOrEmpty(true, HomeFragment.PageState.NETWORK)
            }
        }
    }


    private fun checkConnectionOrEmpty(isShownError: Boolean, state: HomeFragment.PageState) {
        binding?.apply {
            if (isShownError) {
                homeDisLay.isVisible(true, detailContentLay)
                when (state) {
                    HomeFragment.PageState.EMPTY -> {
                        detailContentLay.visibility = View.GONE
                        homeDisLay.visibility = View.VISIBLE
                        disconnectLay.imgDisconnect.setAnimation(R.raw.empty)
                        disconnectLay.imgDisconnect.playAnimation()
                    }
                    HomeFragment.PageState.NETWORK -> {
                        detailContentLay.visibility = View.GONE
                        homeDisLay.visibility = View.VISIBLE
                        disconnectLay.imgDisconnect.setAnimation(R.raw.nointernet)
                        disconnectLay.imgDisconnect.playAnimation()
                    }
                    else -> {}
                }
            } else {
                homeDisLay.isVisible(false, detailContentLay)
            }
        }
    }

}