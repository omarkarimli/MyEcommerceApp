package com.omarkarimli.myecommerceapp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.omarkarimli.myecommerceapp.data.source.local.Converters

@Entity
@TypeConverters(Converters::class)
data class ProductModel (
    @PrimaryKey(autoGenerate = true)
    val localId: Int? = null,

    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("productDetails")
    val productDetails: ProductDetail? = null,
    @SerializedName("seller")
    val seller: String? = null,
    @SerializedName("title")
    val title: String? = null,
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
    val discount: Double? = null,
    @SerializedName("images")
    val images: ArrayList<String?>? = null,
    @SerializedName("originalPrice")
    val originalPrice: Double? = null,
    @SerializedName("outOfStock")
    val outOfStock: Boolean? = null,

    val isCarted: Boolean = false,
    var isBookmarked: Boolean = false,
    val selectedColor: String? = null,

    val numberOfProduct: Int = 1,
    val discountedPrice: Double = 0.0,
    val totalPrice: Double = 0.0
)