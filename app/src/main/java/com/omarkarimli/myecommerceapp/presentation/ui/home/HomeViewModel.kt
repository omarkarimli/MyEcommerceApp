package com.omarkarimli.myecommerceapp.presentation.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.omarkarimli.myecommerceapp.models.CategoryModel
import com.omarkarimli.myecommerceapp.models.ProductModel
import com.omarkarimli.myecommerceapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val provideFirestore: FirebaseFirestore,
    private val provideUserId: String?
) : ViewModel() {

    private val _categories = MutableLiveData<List<CategoryModel>>()
    val categories: LiveData<List<CategoryModel>> = _categories

    private val _products = MutableLiveData<List<ProductModel>>()
    private val _filteredProducts = MutableLiveData<List<ProductModel>>()
    val filteredProducts: LiveData<List<ProductModel>> = _filteredProducts

    private val _bookmarkedIds = MutableLiveData<List<Int>>()
    val bookmarkedIds: LiveData<List<Int>> = _bookmarkedIds

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()
    val success: MutableLiveData<String> = MutableLiveData()

    init {
        fetchBookmarkedIds()
        fetchCategories()
    }

    private fun fetchProducts() {
        loading.value = true

        provideFirestore
            .collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { snapshot ->
                val productList = snapshot.toObjects(ProductModel::class.java)
//                _products.value = productList
//                _filteredProducts.value = productList // Initially display all products

                val newList = productList.map {
                    if (bookmarkedIds.value?.contains(it.id) == true) {
                        it.copy(isBookmarked = true)
                    } else {
                        it
                    }
                }

                _products.value = newList
                _filteredProducts.value = newList

                loading.value = false
            }
            .addOnFailureListener { exception ->
                error.value = "Error fetching products: ${exception.message}"
            }
    }

    private fun fetchBookmarkedIds() {
        provideUserId?.let { uid ->
            provideFirestore
                .collection(Constants.USERS)
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    _bookmarkedIds.value = document.get(Constants.BOOKMARKED_IDS) as? List<Int> ?: emptyList()

                    fetchProducts()
                }
                .addOnFailureListener { exception ->
                    error.value = "Error fetching bookmarks: ${exception.message}"
                }
        }
    }

    private fun fetchCategories() {
        loading.value = true

        provideFirestore
            .collection(Constants.CATEGORIES)
            .get()
            .addOnSuccessListener { snapshot ->
                var categoryList = snapshot.toObjects(CategoryModel::class.java)

                // Add "All" category at the beginning
                categoryList.add(0, CategoryModel(0, Constants.ALL))

                _categories.value = categoryList

                loading.value = false
            }
            .addOnFailureListener { exception ->
                error.value = "Error fetching categories: ${exception.message}"
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
        val currentBookmarks = bookmarkedIds.value?.toMutableList() ?: mutableListOf()

        if (currentBookmarks.contains(productId)) {
            currentBookmarks.remove(productId)
        } else {
            currentBookmarks.add(productId)
        }

        // Update Firestore
        provideUserId?.let { uid ->
            provideFirestore
                .collection(Constants.USERS)
                .document(uid)
                .update(Constants.BOOKMARKED_IDS, currentBookmarks)
                .addOnSuccessListener {
                    _bookmarkedIds.value = currentBookmarks

                    success.value = "Bookmark updated successfully"
                }
                .addOnFailureListener { exception ->
                    error.value = "Error updating bookmarks: ${exception.message}"
                }
        }
    }

}