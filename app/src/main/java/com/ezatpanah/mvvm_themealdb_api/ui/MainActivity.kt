package com.ezatpanah.mvvm_themealdb_api.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ezatpanah.mvvm_themealdb_api.R
import com.ezatpanah.mvvm_themealdb_api.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}