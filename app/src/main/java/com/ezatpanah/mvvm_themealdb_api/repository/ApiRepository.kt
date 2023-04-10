package com.ezatpanah.mvvm_themealdb_api.repository

import com.ezatpanah.mvvm_themealdb_api.api.ApiServices
import com.ezatpanah.mvvm_themealdb_api.response.CategoriesListResponse
import com.ezatpanah.mvvm_themealdb_api.response.FoodsListResponse
import com.ezatpanah.mvvm_themealdb_api.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiServices: ApiServices) {


    suspend fun getRandomFood(): Flow<Response<FoodsListResponse>> {
        return flow {
            emit(apiServices.getFoodRandom())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCategoriesList(): Flow<DataStatus<CategoriesListResponse>> {
        return flow {
            emit(DataStatus.loading())
            //Response
            when (apiServices.getCategoriesList().code()) {
                in 200..202 -> {
                    emit(DataStatus.success(apiServices.getCategoriesList().body()))
                }
                422 -> {
                    emit(DataStatus.error(""))
                }
                in 400..499 -> {
                    emit(DataStatus.error(""))
                }
                in 500..599 -> {
                    emit(DataStatus.error(""))
                }
            }
        }.catch { emit(DataStatus.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }

    suspend fun getFoodsList(letter: String): Flow<DataStatus<FoodsListResponse>> {
        return flow {
            emit(DataStatus.loading())
            when (apiServices.getFoodList(letter).code()) {
                in 200..202 -> {
                    emit(DataStatus.success(apiServices.getFoodList(letter).body()))
                }
            }
        }.catch { emit(DataStatus.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }




}