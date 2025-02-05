package com.omarkarimli.myecommerceapp.presentation.ui.bookmark

import androidx.lifecycle.LiveData
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

    private val _categories = MutableLiveData<List<CategoryModel>>()
    val categories: LiveData<List<CategoryModel>> = _categories

    private val _products = MutableLiveData<List<ProductModel>>()
    private val _filteredProducts = MutableLiveData<List<ProductModel>>()
    val filteredProducts: LiveData<List<ProductModel>> = _filteredProducts

    private val _bookmarkedIds = MutableLiveData<List<Int>>()
    private val bookmarkedIds: LiveData<List<Int>> = _bookmarkedIds

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()
    val success: MutableLiveData<String> = MutableLiveData()

    init {
        fetchCategories()
        fetchBookmarkedIds()
    }

    private fun fetchProducts() {
        loading.value = true

        viewModelScope.launch {
            try {
                val result = provideRepo.fetchProducts()
                val productList = result.toObjects(ProductModel::class.java)

                var newList = productList.map { product ->
                    val isBookmarked = bookmarkedIds.value?.any { it == product.id } ?: false
                    product.copy(isBookmarked = isBookmarked)
                }

                newList = newList.filter { it.isBookmarked }

                _products.value = newList
                _filteredProducts.value = newList

                loading.value = false
            } catch (e: Exception) {
                error.value = "Error fetching products: ${e.message}"
            }
        }
    }

    fun fetchBookmarkedIds() {
        viewModelScope.launch {
            try {
                val result = provideRepo.fetchBookmarkedIds()
                _bookmarkedIds.value = result

                fetchProducts()
            } catch (e: Exception) {
                error.value = "Error fetching bookmarks: ${e.message}"
            }
        }
    }

    private fun fetchCategories() {
        loading.value = true

        viewModelScope.launch {
            try {
                val result = provideRepo.fetchCategories()
                val categoryList = result.toObjects(CategoryModel::class.java)

                // Add "All" category at the beginning
                categoryList.add(0, CategoryModel(0, Constants.ALL))

                _categories.value = categoryList

                loading.value = false
            } catch (e: Exception) {
                error.value = "Error fetching categories: ${e.message}"
            }
        }
    }

    fun filterProductsByCategory(categoryName: String) {
        val allProducts = _products.value ?: return
        _filteredProducts.value = if (categoryName == Constants.ALL) {
            allProducts // Show all products
        } else {
            allProducts.filter { it.category == categoryName }
        }
    }

    fun toggleBookmark(productId: Int) {
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

}