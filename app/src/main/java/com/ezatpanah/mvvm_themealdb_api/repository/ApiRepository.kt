package com.ezatpanah.mvvm_themealdb_api.repository

import com.ezatpanah.mvvm_themealdb_api.api.ApiServices
import com.ezatpanah.mvvm_themealdb_api.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiServices: ApiServices) {


    suspend fun getRandomFood() = flow {
        emit(DataStatus.loading())
        val result = apiServices.getFoodRandom()
        when (result.code()) {
            200 -> emit(DataStatus.success(result.body()))
            400 -> emit(DataStatus.error(result.message()))
            500 -> emit(DataStatus.error(result.message()))
        }
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    suspend fun getCategoriesList() = flow {
        emit(DataStatus.loading())
        val result = apiServices.getCategoriesList()
        when (result.code()) {
            200 -> emit(DataStatus.success(result.body()))
            400 -> emit(DataStatus.error(result.message()))
            500 -> emit(DataStatus.error(result.message()))
        }
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    suspend fun getFoodsList(letter: String) = flow {
        emit(DataStatus.loading())
        val result = apiServices.getFoodList(letter)
        when (result.code()) {
            in 200..202 -> emit(DataStatus.success(result.body()))
            400 -> emit(DataStatus.error(result.message()))
            500 -> emit(DataStatus.error(result.message()))
        }
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }.flowOn(Dispatchers.IO)


}