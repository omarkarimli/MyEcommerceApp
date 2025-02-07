package com.omarkarimli.myecommerceapp.presentation.ui.bookmark

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.myecommerceapp.domain.models.CategoryModel
import com.omarkarimli.myecommerceapp.domain.models.ProductModel
import com.omarkarimli.myecommerceapp.domain.repository.MyEcommerceRepository
import com.omarkarimli.myecommerceapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val provideRepo: MyEcommerceRepository
) : ViewModel() {

    val categories: MutableLiveData<List<CategoryModel>> = MutableLiveData()
    val filteredProducts: MutableLiveData<List<ProductModel>> = MutableLiveData()
    private val products: MutableLiveData<List<ProductModel>> = MutableLiveData()
    private val bookmarkedIds: MutableLiveData<List<Int>> = MutableLiveData()

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()
    val success: MutableLiveData<String> = MutableLiveData()

    fun fetchCategories() {
        loading.value = true

        viewModelScope.launch {
            try {
                val result = provideRepo.fetchCategories()
                categories.value = result

                fetchBookmarkedIds()

                loading.value = false
            } catch (e: Exception) {
                error.value = "Error fetching categories: ${e.message}"
            }
        }
    }

    private fun fetchBookmarkedIds() {
        viewModelScope.launch {
            try {
                val result = provideRepo.fetchBookmarkedIds()
                bookmarkedIds.value = result

                fetchProducts()
            } catch (e: Exception) {
                error.value = "Error fetching bookmarks: ${e.message}"
            }
        }
    }

    private fun fetchProducts() {
        loading.value = true

        viewModelScope.launch {
            try {
                val result = provideRepo.fetchProducts().filter { it.id in bookmarkedIds.value.orEmpty() }
                products.value = result
                filteredProducts.value = result

                loading.value = false
            } catch (e: Exception) {
                error.value = "Error fetching products: ${e.message}"
                Log.e("555", e.message!!)
            }
        }
    }

    fun filterProductsByCategory(categoryName: String) {
        val allProducts = products.value ?: return
        filteredProducts.value = if (categoryName == Constants.ALL) {
            allProducts
        } else {
            allProducts.filter { it.category == categoryName }
        }
    }

    fun toggleBookmark(productId: Int) {
        viewModelScope.launch {
            try {
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

                provideRepo.updateBookmark(newBookmarks)
                bookmarkedIds.value = newBookmarks
                fetchBookmarkedIds()

                success.value = "Bookmark updated successfully"
            } catch (e: Exception) {
                error.value = "Error updating bookmarks: ${e.message}"
            }
        }
    }
}