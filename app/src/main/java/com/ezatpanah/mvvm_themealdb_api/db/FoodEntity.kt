package com.ezatpanah.mvvm_themealdb_api.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ezatpanah.mvvm_themealdb_api.utils.Constant.FOOD_TABLE

@Entity(tableName = FOOD_TABLE)
data class FoodEntity(
    @PrimaryKey
    var id: Int = 0,
    var title: String = "",
    var img: String = ""
)