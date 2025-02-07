package com.omarkarimli.myecommerceapp.adapters

import android.graphics.Paint
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.myecommerceapp.R
import com.omarkarimli.myecommerceapp.databinding.ItemProductBinding
import com.omarkarimli.myecommerceapp.domain.models.ProductModel
import com.omarkarimli.myecommerceapp.utils.loadFirstImage

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    lateinit var onItemClick: (ProductModel) -> Unit
    lateinit var onBookmarkClick: (Int) -> Unit

    private var productList = arrayListOf<ProductModel>()

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layout = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.binding.apply {
            textViewTitle.text = product.title

            if (product.discount != null) {
                buttonDiscount.text = "${product.discount}%"

                textViewPriceDiscounted.text = "$${product.discountedPrice}"

                textViewPriceOriginal.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                textViewPriceOriginal.text = "$${product.originalPrice}"
            }

            imageViewProduct.loadFirstImage(product.images)

            // Update button state based on isBookmarked
            val isBookmarked = product.isBookmarked
            buttonBookmark.icon = ContextCompat.getDrawable(
                buttonBookmark.context,
                if (isBookmarked) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
            )

            //buttonBookmark.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.unbookmarked))

            // Handle bookmark button click
            buttonBookmark.setOnClickListener { onBookmarkClick(product.id!!) }
            root.setOnClickListener { onItemClick(product) }
        }
    }

    fun updateProductList(newList: List<ProductModel>) {
        productList.clear()
        productList.addAll(newList)
        notifyDataSetChanged()
    }
}
