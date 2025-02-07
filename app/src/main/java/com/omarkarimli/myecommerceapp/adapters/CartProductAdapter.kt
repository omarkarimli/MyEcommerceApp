package com.omarkarimli.myecommerceapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.myecommerceapp.databinding.ItemCartProductBinding
import com.omarkarimli.myecommerceapp.domain.models.ProductModel
import com.omarkarimli.myecommerceapp.utils.loadFirstImage
import java.math.BigDecimal
import java.math.RoundingMode

class CartProductAdapter: RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>() {

    lateinit var onItemClick: (ProductModel) -> Unit
    lateinit var onNumberOfProductClick: (ProductModel, Int) -> Unit
    lateinit var onDeleteClick: (ProductModel) -> Unit

    private var cartProductList = arrayListOf<ProductModel>()

    inner class CartProductViewHolder(val binding: ItemCartProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        val layout = ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartProductViewHolder(layout)
    }

    override fun getItemCount(): Int = cartProductList.size

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val cartProduct = cartProductList[position]

        holder.binding.apply {
            textViewTitle.text = cartProduct.title
            textViewProductBrand.text = cartProduct.brand
            textViewProductTotalPrice.text = "$${cartProduct.totalPrice}"

            textViewNumberOfProduct.text = "${cartProduct.numberOfProduct}"
            buttonMinus.isEnabled = cartProduct.numberOfProduct > 1

            imageViewProduct.loadFirstImage(cartProduct.images)

            root.setOnClickListener { onItemClick(cartProduct) }
            imageViewRemove.setOnClickListener { onDeleteClick(cartProduct) }
            buttonPlus.setOnClickListener { onNumberOfProductClick(cartProduct, 1) }
            buttonMinus.setOnClickListener { onNumberOfProductClick(cartProduct, -1) }
        }
    }

    fun updateCartProductList(newList: List<ProductModel>) {
        cartProductList.clear()
        cartProductList.addAll(newList)
        notifyDataSetChanged()
    }
}