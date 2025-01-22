package com.omarkarimli.myecommerceapp.presentation.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.omarkarimli.myecommerceapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val provideFirestore: FirebaseFirestore,
    private val provideAuth: FirebaseAuth,
    private val provideUserId: String?
): ViewModel() {

    val nameSurname: MutableLiveData<String> = MutableLiveData()
    val isNavigating: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        loading.value = true

        if (provideUserId != null) {
            provideFirestore
                .collection(Constants.USERS)
                .document(provideUserId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        loading.value = false

                        // Fetch and display Name and Surname
                        val name = document.getString("name") ?: "Unknown"
                        val surname = document.getString("surname") ?: "Unknown"

                        nameSurname.value = "$name $surname"
                    } else {
                        error.value = "User not found, Signing out..."
                        signOutAndRedirect()
                    }
                }
                .addOnFailureListener { exception ->
                    error.value = "Error fetching user data: ${exception.localizedMessage}"
                    signOutAndRedirect()
                }
        } else {
            error.value = "User not logged in, Signing out..."
            signOutAndRedirect()
        }
    }

    fun signOutAndRedirect() {
        sharedPreferences.edit().clear().apply()

        provideAuth.signOut()
        error.value = "Signing out..."
        isNavigating.value = true
    }
}