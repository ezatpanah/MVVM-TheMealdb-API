package com.ezatpanah.mvvm_themealdb_api.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.ezatpanah.mvvm_themealdb_api.databinding.FragmentHomeBinding
import com.ezatpanah.mvvm_themealdb_api.utils.DataStatus
import com.ezatpanah.mvvm_themealdb_api.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

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
                viewModel.getRandomFood()
                viewModel.foodList.observe(viewLifecycleOwner) { Status ->
                    when (Status) {
                        is DataStatus.Loading -> {}
                        is DataStatus.Success -> {
                            Status.data?.meals?.get(0)!!.let{ meal->
                                headerImg.load(meal.strMealThumb) {
                                    crossfade(true)
                                    crossfade(500)
                                }
                            }

                        }

                        is DataStatus.Error -> {

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