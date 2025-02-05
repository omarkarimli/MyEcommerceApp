package com.omarkarimli.myecommerceapp.presentation.ui.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.myecommerceapp.domain.models.ColorModel
import com.omarkarimli.myecommerceapp.domain.models.ProductModel
import com.omarkarimli.myecommerceapp.domain.repository.MyEcommerceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val provideRepo: MyEcommerceRepository
) : ViewModel() {

    private val _colorModels = MutableLiveData<List<ColorModel>?>()
    val colorModels: MutableLiveData<List<ColorModel>?> = _colorModels

    private val _bookmarkedIds = MutableLiveData<List<Int>>()
    private val bookmarkedIds: LiveData<List<Int>> = _bookmarkedIds

    val totalOriginalPrice: MutableLiveData<Double> = MutableLiveData(0.0)
    val totalDiscountedPrice: MutableLiveData<Double> = MutableLiveData(0.0)

    val numberOfProduct: MutableLiveData<Int> = MutableLiveData(1)
    val productId: MutableLiveData<Int> = MutableLiveData()
    val product: MutableLiveData<ProductModel?> = MutableLiveData()
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
                _bookmarkedIds.value = result

                getProductById(productId.value!!)
            } catch (e: Exception) {
                error.value = "Error fetching bookmarks: ${e.message}"
            }
        }
    }

    private fun getProductById(id: Int) {
        loading.value = true

        viewModelScope.launch {
            try {
                val result = provideRepo.getProductById(id)

                val isBookmarked = bookmarkedIds.value?.any { it == result.id } ?: false
                product.value = result.copy(
                    isBookmarked = isBookmarked
                )

                val colorModels = product.value?.productDetails?.color?.map { colorString ->
                    ColorModel(colorString, false)
                } ?: emptyList()
                _colorModels.value = colorModels

                changeTotalPrice()

                loading.value = false
            } catch (e: Exception) {
                error.value = e.localizedMessage
            }
        }
    }

    fun toggleBookmark(productId: Int) {
        // Initialize or copy the current bookmarks as a mutable list
        val newBookmarks = _bookmarkedIds.value?.toMutableList() ?: mutableListOf()

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

        viewModelScope.launch {
            try {
                provideRepo.updateBookmark(newBookmarks)

                _bookmarkedIds.value = newBookmarks
                fetchBookmarkedIds()
                success.value = "Bookmark updated successfully"
            } catch (e: Exception) {
                error.value = "Error updating bookmarks: ${e.message}"
            }
        }
    }

    fun toggleColor(colorModel: ColorModel) {
        val currentColorModels = _colorModels.value?.toMutableList() ?: mutableListOf()

        Log.e("toggleColor", "Current Colors: $currentColorModels")

        val newColorModels = currentColorModels.map {
            it.copy(isSelected = it.colorString == colorModel.colorString)
        }

        _colorModels.value = newColorModels

        Log.e("toggleColor", "Updated Colors: $newColorModels")
    }

    fun changeNumberOfProduct(n: Int) {
        val currentNumber = numberOfProduct.value ?: 1
        numberOfProduct.value = currentNumber + n

        changeTotalPrice()
    }

    fun changeTotalPrice() {
        val numberOfProduct = numberOfProduct.value!!.toDouble()
        val currentTotalOriginalPrice = product.value?.originalPrice!! * numberOfProduct
        val currentTotalDiscountedPrice = product.value?.originalPrice!! * numberOfProduct * (100 - product.value?.discount!!) / 100

        totalOriginalPrice.value = currentTotalOriginalPrice
        totalDiscountedPrice.value = currentTotalDiscountedPrice
    }

    fun addProductToCart(productModel: ProductModel) {

    }
}