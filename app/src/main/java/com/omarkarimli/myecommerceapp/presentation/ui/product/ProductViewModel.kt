package com.omarkarimli.myecommerceapp.presentation.ui.product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.myecommerceapp.domain.models.ColorModel
import com.omarkarimli.myecommerceapp.domain.models.ProductModel
import com.omarkarimli.myecommerceapp.domain.repository.MyEcommerceRepository
import com.omarkarimli.myecommerceapp.utils.Constants
import com.omarkarimli.myecommerceapp.utils.roundDouble
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    val provideRepo: MyEcommerceRepository
) : ViewModel() {

    val colorModels: MutableLiveData<List<ColorModel>?> = MutableLiveData()
    private val bookmarkedIds: MutableLiveData<List<Int>> = MutableLiveData()
    val totalOriginalPrice: MutableLiveData<Double> = MutableLiveData(0.0)
    val totalDiscountedPrice: MutableLiveData<Double> = MutableLiveData(0.0)
    var numberOfProduct: MutableLiveData<Int> = MutableLiveData(1)
    val product: MutableLiveData<ProductModel?> = MutableLiveData()

    val argsProductId: MutableLiveData<Int> = MutableLiveData()
    val argsSource: MutableLiveData<String> = MutableLiveData()

    val navigatingTo: MutableLiveData<String> = MutableLiveData()

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()
    val success: MutableLiveData<String> = MutableLiveData()

    init {
        fetchBookmarkedIds()
    }

    private fun fetchBookmarkedIds() {
        viewModelScope.launch {
            try {
                val result = provideRepo.fetchBookmarkedIds()
                bookmarkedIds.value = result

                if (argsSource.value == Constants.CART) {
                    Log.e("555", "From Cart")
                    // from Cart
                    getProductByIdFromLocal(argsProductId.value!!)
                } else {
                    Log.e("555", "From Home")
                    // from Home
                    getProductByIdFromRemote(argsProductId.value!!)
                }
            } catch (e: Exception) {
                error.value = "Error: ${e.message}"
            }
        }
    }

    private fun getProductByIdFromLocal(id: Int) {
        loading.value = true

        viewModelScope.launch {
            try {
                val result = provideRepo.getProductByIdFromLocal(id)
                product.value = result

                changeColors()
                changeNumberOfProduct(0)

                loading.value = false
            } catch (e: Exception) {
                error.value = e.localizedMessage
            }
        }
    }

    private fun getProductByIdFromRemote(id: Int) {
        loading.value = true

        viewModelScope.launch {
            try {
                val result = provideRepo.getProductById(id)
                product.value = result

                changeColors()
                changeNumberOfProduct(0)

                loading.value = false
            } catch (e: Exception) {
                error.value = e.localizedMessage
            }
        }
    }

    private fun changeColors() {
        val list = product.value?.productDetails?.color?.map { colorString ->
            ColorModel(
                colorString,
                colorString == product.value?.selectedColor
            )
        } ?: emptyList()
        colorModels.value = list
    }

    fun changeNumberOfProduct(n: Int) {
        val currentNumber = numberOfProduct.value ?: 1
        numberOfProduct.value = currentNumber + n

        changePrice()
    }

    private fun changePrice() {
        val currentTotalOriginalPrice = product.value?.originalPrice!! * numberOfProduct.value!!.toDouble()
        val currentTotalDiscountedPrice = product.value?.discountedPrice!! * numberOfProduct.value!!.toDouble()

        totalOriginalPrice.value = roundDouble(currentTotalOriginalPrice)
        totalDiscountedPrice.value = roundDouble(currentTotalDiscountedPrice)

        Log.e("555", totalDiscountedPrice.value.toString())

        if (product.value?.isCarted == true) {
            product.value = product.value?.copy(
                totalPrice = totalDiscountedPrice.value!!,
                numberOfProduct = numberOfProduct.value!!,
            )
        }
    }

    fun toggleBookmark(productId: Int) {
        viewModelScope.launch {
            try {
                // Initialize or copy the current bookmarks as a mutable list
                val newBookmarks = bookmarkedIds.value?.toMutableList() ?: mutableListOf()

                if (newBookmarks.isEmpty() || !newBookmarks.any { it == productId }) {
                    newBookmarks.add(productId)
                } else {
                    // index always makes -1 problem
                    for (i in newBookmarks.indices) {
                        if (newBookmarks[i] == productId) {
                            newBookmarks.removeAt(i)
                            break
                        }
                    }
                }

                val productCopy = product.value!!.copy(isBookmarked = newBookmarks.any { it == product.value?.id })
//                product.value = productCopy
                product.value?.let { currentProduct ->
                    currentProduct.isBookmarked = newBookmarks.any { it == currentProduct.id }
                    product.postValue(currentProduct) // This avoids creating a new object
                }

                // Update Remotely
                provideRepo.updateBookmark(newBookmarks)
                bookmarkedIds.value = newBookmarks

                // Update Locally
                if (argsSource.value == Constants.CART) {
                    provideRepo.updateProductLocally(productCopy)
                }

                success.value = "Bookmark updated successfully"
            } catch (e: Exception) {
                error.value = "Error updating bookmarks: ${e.message}"
            }
        }
    }

    fun toggleColor(colorModel: ColorModel) {
        val currentColorModels = colorModels.value?.toMutableList() ?: mutableListOf()

        val newColorModels = currentColorModels.map {
            it.copy(isSelected = it.colorString == colorModel.colorString)
        }

        colorModels.value = newColorModels

        if (argsSource.value == Constants.CART) {
            product.value = product.value?.copy(selectedColor = colorModel.colorString)
        }
    }

    fun toggleProductInCart(productModel: ProductModel) {
        viewModelScope.launch {
            try {
                val selectedColor = colorModels.value?.find { it.isSelected }?.colorString
                var productCopy = productModel.copy(
                    totalPrice = totalDiscountedPrice.value!!,
                    numberOfProduct = numberOfProduct.value!!,
                    selectedColor = selectedColor
                )

                if (!productModel.isCarted) {
                    if (selectedColor != null) {
                        productCopy = productCopy.copy(
                            isCarted = true
                        )
                        provideRepo.addProductToLocal(productCopy)
                        success.value = "Product added to cart"

                        navigatingTo.value = Constants.CART
                    } else {
                        error.value = "Please select a color"
                    }
                } else {
                    product.value = productCopy
                    provideRepo.deleteProductFromLocal(productModel)
                    success.value = "Product removed from cart"

                    navigatingTo.value = Constants.CART
                }
            } catch (e: Exception) {
                error.value = "Something went wrong on clicking Add to Cart button"
                Log.e("555", e.message!!)
            }
        }
    }

    fun updateProductLocally(productModel: ProductModel) {
        viewModelScope.launch {
            try {
                provideRepo.updateProductLocally(productModel)
            } catch (e: Exception) {
                error.value = "Something went wrong on Updating"
                Log.e("444", e.message!!)
            }
        }
    }
}