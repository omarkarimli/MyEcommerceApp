package com.omarkarimli.myecommerceapp.domain.models

import com.google.gson.annotations.SerializedName

data class ProductDetail(
    @SerializedName("closure")
    val closure: String? = null,
    @SerializedName("color")
    val color: ArrayList<String?>? = null,
    @SerializedName("fabric")
    val fabric: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("pattern")
    val pattern: String? = null,
    @SerializedName("pockets")
    val pockets: String? = null,
    @SerializedName("styleCode")
    val styleCode: String? = null
)