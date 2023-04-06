package com.ezatpanah.mvvm_themealdb_api.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.ezatpanah.mvvm_themealdb_api.databinding.FragmentHomeBinding
import com.ezatpanah.mvvm_themealdb_api.utils.DataStatus
import com.ezatpanah.mvvm_themealdb_api.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

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
            viewModel.getRandomFood()
            viewModel.foodList.observe(viewLifecycleOwner) {
                when (it.status) {
                    DataStatus.Status.LOADING -> {
                        //pBarLoading.isVisible(true,rvCrypto)
                    }
                    DataStatus.Status.SUCCESS -> {
                        //pBarLoading.isVisible(false,rvCrypto)
                        it.data?.let { meal ->
                            headerImg.load(meal?.strMealThumb) {
                                crossfade(true)
                                crossfade(500)
                            }
                        }
                    }
                    DataStatus.Status.ERROR -> {
                       //pBarLoading.isVisible(true,rvCrypto)
                        Toast.makeText(requireContext(), "There is something wrong!", Toast.LENGTH_SHORT).show()
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