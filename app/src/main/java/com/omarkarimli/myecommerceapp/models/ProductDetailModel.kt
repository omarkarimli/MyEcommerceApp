package com.omarkarimli.myecommerceapp.models

import com.google.gson.annotations.SerializedName

data class ProductDetailModel(
    @SerializedName("styleCode")
    val styleCode: String? = null,
    @SerializedName("closure")
    val closure: String? = null,
    @SerializedName("pockets")
    val pockets: String? = null,
    @SerializedName("fabric")
    val fabric: String? = null,
    @SerializedName("pattern")
    val pattern: String? = null,
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("material")
    val material: String? = null,
    @SerializedName("soleMaterial")
    val soleMaterial: String? = null
)