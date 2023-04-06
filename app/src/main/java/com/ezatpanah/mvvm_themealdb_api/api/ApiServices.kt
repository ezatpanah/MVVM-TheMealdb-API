package com.ezatpanah.mvvm_themealdb_api.api

import com.ezatpanah.mvvm_themealdb_api.response.CategoriesListResponse
import com.ezatpanah.mvvm_themealdb_api.response.FoodsListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    //https://www.themealdb.com/api.php
    /**
    Header
    Lookup a single random meal
    www.themealdb.com/api/json/v1/1/random.php
    Categories
    List all meal categories
    www.themealdb.com/api/json/v1/1/categories.php
    Search meal by name
    www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
    List all meals by first letter
    www.themealdb.com/api/json/v1/1/search.php?f=a
    Filter by Category
    www.themealdb.com/api/json/v1/1/filter.php?c=Seafood
    Lookup full meal details by id
    www.themealdb.com/api/json/v1/1/lookup.php?i=52772
     **/

    @GET("random.php")
     suspend fun getFoodRandom() : Response<FoodsListResponse.Meal>

    @GET("categories.php")
    suspend fun getCategoriesList() : Response<CategoriesListResponse>

    @GET("search.php")
    suspend fun getFoodList(@Query("f") latter : String) : Response<FoodsListResponse>

    @GET("search.php")
    suspend fun searchList(@Query("s") latter : String) : Response<FoodsListResponse>

    @GET("filter.php")
    suspend fun filterList(@Query("c") latter : String) : Response<FoodsListResponse>

    @GET("lookup.php")
    suspend fun getFoodDetails(@Query("i") id : Int) : Response<FoodsListResponse>
}