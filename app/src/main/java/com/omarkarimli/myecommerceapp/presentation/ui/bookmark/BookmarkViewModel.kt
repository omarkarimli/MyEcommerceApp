package com.omarkarimli.myecommerceapp.presentation.ui.bookmark

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.omarkarimli.myecommerceapp.models.ProductModel
import com.omarkarimli.myecommerceapp.utils.Constants

class BookmarkViewModel : ViewModel() {

    val productList: MutableLiveData<List<ProductModel>> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    private val firestore = FirebaseFirestore.getInstance()

    init {
        getAllProducts()
    }

    private fun getAllProducts() {
        loading.value = true

        firestore
            .collection(Constants.PRODUCTS)
//            .orderBy("originalPrice")
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    productList.value = snapshot.toObjects(ProductModel::class.java)

                    loading.value = false
                }
            }
            .addOnFailureListener { exception ->
                error.value = exception.localizedMessage
            }
    }

}