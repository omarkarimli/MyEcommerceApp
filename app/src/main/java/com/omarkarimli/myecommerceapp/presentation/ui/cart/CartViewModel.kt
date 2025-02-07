package com.omarkarimli.myecommerceapp.presentation.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.myecommerceapp.domain.models.ProductModel
import com.omarkarimli.myecommerceapp.domain.repository.MyEcommerceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val provideRepo: MyEcommerceRepository
): ViewModel() {

    val totalCartPrice = MutableLiveData<Double>()
    val cartProductList = MutableLiveData<List<ProductModel>>()

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()
    val success: MutableLiveData<String> = MutableLiveData()

    init {
        fetchCartProducts()
    }

    fun calculateTotalCartPrice(cartProductList: List<ProductModel>) {
        totalCartPrice.value = cartProductList.sumOf { it.totalPrice }
    }

    fun fetchCartProducts() {
        loading.value = true

        viewModelScope.launch {
            try {
                loading.value = false

                val cartProducts = provideRepo.getAllProductsFromLocal()
                cartProductList.value = cartProducts
            } catch (e: Exception) {
                error.value = "Error fetching cart products: ${e.message}"
            }
        }
    }

    fun changeNumberOfProduct(cartProduct: ProductModel, n: Int) {
        viewModelScope.launch {
            try {
                val numberOfProduct = cartProduct.numberOfProduct + n
                val totalProductPrice = cartProduct.totalPrice + n * cartProduct.discountedPrice

                val cartProductCopy = cartProduct.copy(
                    numberOfProduct = numberOfProduct,
                    totalPrice = String.format("%.2f", totalProductPrice).toDouble()
                )
                provideRepo.updateProductLocally(cartProductCopy)
                cartProductList.value = provideRepo.getAllProductsFromLocal()
            } catch (e: Exception) {
                error.value = "Error updating product: ${e.message}"
            }
        }
    }

    fun deleteProduct(cartProduct: ProductModel) {
        viewModelScope.launch {
            try {
                provideRepo.deleteProductFromLocal(cartProduct)

                cartProductList.value = provideRepo.getAllProductsFromLocal()
            } catch (e: Exception) {
                error.value = "Error deleting product: ${e.message}"
            }
        }
    }
}