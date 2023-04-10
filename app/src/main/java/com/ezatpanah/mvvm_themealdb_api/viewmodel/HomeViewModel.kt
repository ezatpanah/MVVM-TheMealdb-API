package com.ezatpanah.mvvm_themealdb_api.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ezatpanah.mvvm_themealdb_api.repository.ApiRepository
import com.ezatpanah.mvvm_themealdb_api.response.CategoriesListResponse
import com.ezatpanah.mvvm_themealdb_api.response.FoodsListResponse
import com.ezatpanah.mvvm_themealdb_api.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val repository: ApiRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val _randomFood: MutableLiveData<List<FoodsListResponse.Meal>> = MutableLiveData()
    val randomFood: LiveData<List<FoodsListResponse.Meal>>
        get() = _randomFood


    fun getRandomFood() = viewModelScope.launch {
        repository.getRandomFood().collect {
            _randomFood.value = it.body()?.meals!!
        }
    }


    private val _categoriesList: MutableLiveData<DataStatus<CategoriesListResponse>> = MutableLiveData()
    val categoriesList: LiveData<DataStatus<CategoriesListResponse>>
        get() = _categoriesList

    fun getCategoriesList() = viewModelScope.launch {
        repository.getCategoriesList().collect {
            _categoriesList.value = it
        }
    }

    val filtersListData = MutableLiveData<MutableList<Char>>()
    fun loadFilterList() = viewModelScope.launch {
        val letters = listOf('A'..'Z').flatten().toMutableList()
        filtersListData.value = letters
    }

    private val _foodList: MutableLiveData<DataStatus<FoodsListResponse>> = MutableLiveData()
    val foodList: LiveData<DataStatus<FoodsListResponse>>
        get() = _foodList

    fun getFoodsList(letter: String) = viewModelScope.launch {
        repository.getFoodsList(letter).collect {
            _foodList.value = it
        }
    }


}