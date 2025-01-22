package com.omarkarimli.myecommerceapp.presentation.ui.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.omarkarimli.myecommerceapp.models.ProductModel
import com.omarkarimli.myecommerceapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val provideFirestore: FirebaseFirestore,
    private val provideAuth: FirebaseAuth,
    private val provideUserId: String?
) : ViewModel() {

    val product: MutableLiveData<ProductModel?> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun getProductById(id: Int) {
        loading.value = true

        provideFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.ID, id)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    product.value = snapshot.documents[0].toObject(ProductModel::class.java)
                } else {
                    error.value = "Product not found"
                }
                loading.value = false
            }
            .addOnFailureListener { exception ->
                error.value = exception.localizedMessage
                loading.value = false
            }
    }
}