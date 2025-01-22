package com.omarkarimli.myecommerceapp.models

import com.google.gson.annotations.SerializedName

data class ProductModel (
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("brand")
    val brand: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("averageRating")
    val averageRating: Double? = null,
    @SerializedName("creationDate")
    val creationDate: String? = null,
    @SerializedName("seller")
    val seller: String? = null,
    @SerializedName("discount")
    val discount: Int? = null,
    @SerializedName("originalPrice")
    val originalPrice: Double? = null,
    @SerializedName("outOfStock")
    val outOfStock: Boolean? = null,
    @SerializedName("images")
    val images: ArrayList<String?>? = null,
    @SerializedName("productDetails")
    val productDetails: List<ProductDetailModel?>? = null,

    val isBookmarked: Boolean = false
)