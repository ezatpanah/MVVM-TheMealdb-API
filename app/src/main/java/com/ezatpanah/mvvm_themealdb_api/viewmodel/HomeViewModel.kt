package com.ezatpanah.mvvm_themealdb_api.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ezatpanah.mvvm_themealdb_api.repository.ApiRepository
import com.ezatpanah.mvvm_themealdb_api.response.FoodsListResponse
import com.ezatpanah.mvvm_themealdb_api.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val repository: ApiRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _foodList: MutableLiveData<DataStatus<FoodsListResponse>> = MutableLiveData()
    val foodList: LiveData<DataStatus<FoodsListResponse>>
    get() = _foodList


    fun getRandomFood() = viewModelScope.launch {
        _foodList.value = DataStatus.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.getRandomFood()
                _foodList.value = handleRandomFood(response)

            } catch (_: Exception) {
                _foodList.value = DataStatus.Error("Recipes not found")

            }
        } else {
            _foodList.value = DataStatus.Error("No Internet Connection.")
        }
    }

    private fun handleRandomFood(response: Response<FoodsListResponse>): DataStatus<FoodsListResponse>? {
        return when {
            response.message().toString().contains("timeout") -> {
                DataStatus.Error("Timeout")
            }
            response.code() == 402 -> {
                DataStatus.Error("API key Limited")
            }
            response.isSuccessful -> {
                val randomFood = response.body()
                DataStatus.Success(randomFood!!)
            }
            else -> {
                DataStatus.Error(response.message())
            }
        }
    }

    //Check Internet
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}