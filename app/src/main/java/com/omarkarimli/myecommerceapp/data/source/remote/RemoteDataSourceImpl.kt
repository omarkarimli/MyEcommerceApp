package com.omarkarimli.myecommerceapp.data.source.remote

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.omarkarimli.myecommerceapp.domain.models.ProductModel
import com.omarkarimli.myecommerceapp.utils.Constants
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val provideAuth: FirebaseAuth,
    private val provideFirestore: FirebaseFirestore
) : RemoteDataSource {

    override suspend fun fetchProducts(): QuerySnapshot =
        provideFirestore
            .collection(Constants.PRODUCTS)
            .get()
            .await()

    override suspend fun fetchCategories(): QuerySnapshot =
        provideFirestore
            .collection(Constants.CATEGORIES)
            .get()
            .await()

    override suspend fun fetchBookmarkedIds(): List<Int> {
        val document = provideFirestore
            .collection(Constants.USERS)
            .document(provideAuth.currentUser?.uid ?: "error")
            .get()
            .await()
        val list = document.get(Constants.BOOKMARKED_IDS) as List<Long>
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


    override suspend fun changePassword(currentPassword: String) : Void? {
        val credential = EmailAuthProvider.getCredential(provideAuth.currentUser?.email ?: "error", currentPassword)

        return provideAuth.currentUser
            ?.reauthenticate(credential)
            ?.await()
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

        return document.documents[0].toObject(ProductModel::class.java)!!
    }
}