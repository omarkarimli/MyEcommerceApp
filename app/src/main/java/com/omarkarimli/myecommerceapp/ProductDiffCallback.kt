package com.omarkarimli.myecommerceapp

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.omarkarimli.myecommerceapp.domain.models.ProductModel

class ProductDiffCallback : DiffUtil.ItemCallback<ProductModel>() {
    override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
        return oldItem.id == newItem.id // Products are the same if IDs match
    }

    override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
        return oldItem == newItem // Compare all fields
    }

    override fun getChangePayload(oldItem: ProductModel, newItem: ProductModel): Any? {
        val diffBundle = Bundle()

        if (oldItem.isBookmarked != newItem.isBookmarked) {
            diffBundle.putBoolean("isBookmarked", newItem.isBookmarked)
        }
        if (oldItem.numberOfProduct != newItem.numberOfProduct) {
            diffBundle.putInt("numberOfProduct", newItem.numberOfProduct)
        }
        if (oldItem.totalPrice != newItem.totalPrice) {
            diffBundle.putDouble("totalPrice", newItem.totalPrice)
        }
        if (oldItem.selectedColor != newItem.selectedColor) {
            diffBundle.putString("selectedColor", newItem.selectedColor)
        }

        return if (diffBundle.size() > 0) diffBundle else null
    }
}

