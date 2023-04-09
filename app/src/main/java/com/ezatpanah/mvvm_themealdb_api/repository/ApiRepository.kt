package com.ezatpanah.mvvm_themealdb_api.repository

import com.ezatpanah.mvvm_themealdb_api.api.ApiServices
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiServices: ApiServices) {


    suspend fun getRandomFood() = apiServices.getFoodRandom()

}