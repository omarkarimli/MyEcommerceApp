package com.omarkarimli.myecommerceapp.adapters

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
import com.omarkarimli.myecommerceapp.models.ProductModel
import com.omarkarimli.myecommerceapp.utils.loadFirstImage

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    lateinit var onItemClick: (ProductModel) -> Unit
    lateinit var onBookmarkClick: (Int) -> Unit

    private var productList = arrayListOf<ProductModel>()
    private var bookmarkedIdList = arrayListOf<Int>()

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

            val discount = product.discount ?: 0
            if (product.discount != null) {
                textViewDiscount.text = "-${product.discount}%"

                val priceDiscounted =
                    product.originalPrice!! - (product.originalPrice * discount) / 100

                // Create Spannable for discounted price
                val discountedPriceText = SpannableString("$${priceDiscounted} ")
                discountedPriceText.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.red
                        )
                    ), // Set color red
                    0,
                    discountedPriceText.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                discountedPriceText.setSpan(
                    RelativeSizeSpan(1.2f), // Make discounted price 20% larger
                    0,
                    discountedPriceText.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                // Create Spannable for original price
                val originalPriceText = SpannableString("$${product.originalPrice}")
                originalPriceText.setSpan(
                    StrikethroughSpan(),
                    0,
                    originalPriceText.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                originalPriceText.setSpan(
                    RelativeSizeSpan(1f), // Make original price 20% smaller
                    0,
                    originalPriceText.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                // Combine both parts into a SpannableStringBuilder
                val finalPriceText = SpannableStringBuilder()
                finalPriceText.append(discountedPriceText)
                finalPriceText.append(originalPriceText)

                textViewPrice.text = finalPriceText
            }

            imageViewProduct.loadFirstImage(product.images)

            // Update button state based on isBookmarked
            val isBookmarked = bookmarkedIdList.contains(product.id)
            buttonBookmark.icon = ContextCompat.getDrawable(
                buttonBookmark.context,
                if (isBookmarked) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
            )

            Log.e("444", "isBookmarked: $isBookmarked | Product Id: ${product.id} | $bookmarkedIdList")

            //buttonBookmark.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.unbookmarked))

            // Handle bookmark button click
            buttonBookmark.setOnClickListener {
                onBookmarkClick(product.id!!)
            }

            root.setOnClickListener {
                onItemClick(product)
            }

        }
    }

    fun updateProductList(newList: List<ProductModel>) {
        productList.clear()
        productList.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateBookmarkedProductsId(newList: List<Int>) {
        bookmarkedIdList.clear()
        bookmarkedIdList.addAll(newList)
        notifyDataSetChanged()
    }
}
