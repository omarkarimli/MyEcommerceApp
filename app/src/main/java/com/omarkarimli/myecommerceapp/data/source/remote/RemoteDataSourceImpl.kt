package com.omarkarimli.myecommerceapp.data.source.remote

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.omarkarimli.myecommerceapp.data.source.local.LocalDataSource
import com.omarkarimli.myecommerceapp.domain.models.CategoryModel
import com.omarkarimli.myecommerceapp.domain.models.ProductModel
import com.omarkarimli.myecommerceapp.utils.Constants
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val provideAuth: FirebaseAuth,
    private val provideFirestore: FirebaseFirestore,
    private val provideNumberFormat: NumberFormat
) : RemoteDataSource {

    override suspend fun fetchProducts() : List<ProductModel> {
        val document = provideFirestore
            .collection(Constants.PRODUCTS)
            .get()
            .await()
        var productList = document.toObjects(ProductModel::class.java)

        productList = productList.map { productModel ->
            productModel.copy(
                originalPrice = provideNumberFormat.parse(String.format("%.2f", productModel.originalPrice))?.toDouble(),
                isBookmarked = fetchBookmarkedIds().any { it == productModel.id },
                numberOfProduct = 1,
                discountedPrice = provideNumberFormat.parse(String.format("%.2f", (productModel.originalPrice!! - (productModel.originalPrice * productModel.discount!! / 100))))
                    ?.toDouble() ?: 0.0,
                totalPrice = productModel.discountedPrice,
            )
        }

        return productList
    }

    override suspend fun fetchCategories(): List<CategoryModel> {
        val document = provideFirestore
            .collection(Constants.CATEGORIES)
            .get()
            .await()

        val categoryList = document.toObjects(CategoryModel::class.java)

        // Add "All" category at the beginning
        categoryList.add(0, CategoryModel(0, Constants.ALL))

        return categoryList
    }

    override suspend fun fetchBookmarkedIds(): List<Int> {
        val document = provideFirestore
            .collection(Constants.USERS)
            .document(provideAuth.currentUser?.uid ?: "error")
            .get()
            .await()
        val list = document.get(Constants.BOOKMARKED_IDS) as List<Long>

        localDataSource.getAllLocally().forEach { localProduct ->
            localDataSource.updateProductLocally(
                localProduct.copy(
                    isBookmarked = list.any { it == localProduct.id!!.toLong() }
                )
            )
        }

        return list.map { it.toInt() }
    }

    override suspend fun updateBookmark(newBookmarks: MutableList<Int>) {
        val uid = provideAuth.currentUser?.uid ?: "error"
        provideFirestore
            .collection(Constants.USERS)
            .document(uid)
            .update(Constants.BOOKMARKED_IDS, newBookmarks)
            .await()
    }

    override suspend fun changePassword(email: String, currentPassword: String, newPassword: String) {
        val user = provideAuth.currentUser ?: FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(email, currentPassword)

        try {
            if (user != null) {
                user.reauthenticate(credential).await() // Re-authenticate user
                user.updatePassword(newPassword).await() // Update password
            } else {
                throw Exception("User is not authenticated")
            }
        } catch (e: Exception) {
            throw e // Handle exception appropriately
        }
    }

    override suspend fun fetchUserData(): DocumentSnapshot =
        provideFirestore
            .collection(Constants.USERS)
            .document(provideAuth.currentUser?.uid ?: "error")
            .get()
            .await()

    override suspend fun getProductById(id: Int): ProductModel {
        val document = provideFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.ID, id)
            .get()
            .await()

        var productModel = document.documents[0].toObject(ProductModel::class.java)!!
        productModel = productModel.copy(
            isBookmarked = fetchBookmarkedIds().any { it == productModel.id },
            numberOfProduct = 1,
            discountedPrice = productModel.originalPrice!! - (productModel.originalPrice!! * productModel.discount!! / 100),
            totalPrice = productModel.discountedPrice,
        )
        return productModel
    }

    override suspend fun loginUserAccount(isChecked: Boolean, email: String, password: String): AuthResult =
        provideAuth
            .signInWithEmailAndPassword(email, password)
            .await()

    override suspend fun registerNewUser(email: String, password: String): AuthResult =
        provideAuth
            .createUserWithEmailAndPassword(email, password)
            .await()

    override suspend fun addUserToFirestore(userData: HashMap<String, Serializable>) {
        val uid = provideAuth.currentUser?.uid ?: "error"
        provideFirestore
            .collection(Constants.USERS)
            .document(uid)
            .set(userData)
            .await()
    }
}