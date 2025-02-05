package com.omarkarimli.myecommerceapp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ProductModel (
    @SerializedName("averageRating")
    val averageRating: Double? = null,
    @SerializedName("brand")
    val brand: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("creationDate")
    val creationDate: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("discount")
    val discount: Int? = null,
    @PrimaryKey
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("images")
    val images: ArrayList<String?>? = null,
    @SerializedName("originalPrice")
    val originalPrice: Int? = null,
    @SerializedName("outOfStock")
    val outOfStock: Boolean? = null,
    @SerializedName("productDetails")
    val productDetails: ProductDetail? = null,
    @SerializedName("seller")
    val seller: String? = null,
    @SerializedName("title")
    val title: String? = null,

    val isBookmarked: Boolean = false,
    val selectedColor: String? = null,

    val numberOfProduct: Int = 1,
    val totalPrice: Double = 0.0,
    val isCarted: Boolean = false
)