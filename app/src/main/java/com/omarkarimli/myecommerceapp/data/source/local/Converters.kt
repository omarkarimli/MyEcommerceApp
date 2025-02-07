package com.omarkarimli.myecommerceapp.data.source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omarkarimli.myecommerceapp.domain.models.ProductDetail

class Converters {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String>? {
        if (value == null) {
            return null
        }
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromProductDetail(productDetail: ProductDetail?): String? {
        if (productDetail == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<ProductDetail>() {}.type
        return gson.toJson(productDetail, type)
    }

    @TypeConverter
    fun toProductDetail(productDetailString: String?): ProductDetail? {
        if (productDetailString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<ProductDetail>() {}.type
        return gson.fromJson(productDetailString, type)
    }
}