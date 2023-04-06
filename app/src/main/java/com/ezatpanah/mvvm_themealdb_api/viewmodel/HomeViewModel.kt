package com.ezatpanah.mvvm_themealdb_api.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezatpanah.mvvm_themealdb_api.repository.ApiRepository
import com.ezatpanah.mvvm_themealdb_api.response.FoodsListResponse
import com.ezatpanah.mvvm_themealdb_api.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    private val _foodList = MutableLiveData<DataStatus<FoodsListResponse.Meal>>()
    val foodList: LiveData<DataStatus<FoodsListResponse.Meal>>
        get() = _foodList


    fun getRandomFood() = viewModelScope.launch {
        repository.getRandomFood().collect {
            _foodList.value = it
        }
    }


}