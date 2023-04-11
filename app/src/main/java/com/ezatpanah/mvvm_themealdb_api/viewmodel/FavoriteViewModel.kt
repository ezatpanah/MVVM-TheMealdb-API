package com.ezatpanah.mvvm_themealdb_api.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezatpanah.mvvm_themealdb_api.db.FoodEntity
import com.ezatpanah.mvvm_themealdb_api.repository.MainRepository
import com.ezatpanah.mvvm_themealdb_api.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {


    private val _foodList: MutableLiveData<DataStatus<List<FoodEntity>>> = MutableLiveData()
    val foodList: LiveData<DataStatus<List<FoodEntity>>>
        get() = _foodList

    fun getFavoritesFoodList() = viewModelScope.launch {
        repository.getDbFoodList().collect {
            _foodList.value=DataStatus.success(it)
        }
    }
}