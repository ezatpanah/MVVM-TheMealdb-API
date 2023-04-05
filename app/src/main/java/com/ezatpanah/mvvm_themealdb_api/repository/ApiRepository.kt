package com.ezatpanah.mvvm_themealdb_api.repository

import com.ezatpanah.mvvm_themealdb_api.api.ApiServices
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiServices: ApiServices){

    fun getFoodRandom()=apiServices.getFoodRandom()
    fun getCategoriesList()=apiServices.getCategoriesList()
    fun getFoodList(letter : String)=apiServices.getFoodList(letter)
    fun getSearchList(letter : String)=apiServices.searchList(letter)
    fun getFilterList(letter : String)=apiServices.filterList(letter)
    fun getFoodDetails(id : Int)=apiServices.getFoodDetails(id)

}