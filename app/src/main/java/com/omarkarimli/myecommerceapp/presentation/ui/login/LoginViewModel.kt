package com.omarkarimli.myecommerceapp.presentation.ui.login

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
class LoginViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val provideFirestore: FirebaseFirestore,
    private val provideAuth: FirebaseAuth,
    private val provideUserId: String?
): ViewModel() {

    val isNavigating: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun loginUserAccount(isChecked: Boolean, email: String, password: String) {
        loading.value = true

        if (email.isNotEmpty() && password.isNotEmpty()) {
            provideAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // Fetch user data from Firestore
                        if (provideUserId != null) {
                            provideFirestore
                                .collection(Constants.USERS)
                                .document(provideUserId)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        loading.value = false

                                        val editor = sharedPreferences.edit()
                                        editor?.putBoolean(Constants.IS_LOGGED_KEY, isChecked)
                                        editor?.apply()

                                        // Navigate to the home screen
                                        isNavigating.value = true
                                    } else {
                                        loading.value = false
                                        error.value = "User not found"
                                    }
                                }
                                .addOnFailureListener { e ->
                                    loading.value = false
                                    error.value = e.localizedMessage
                                }
                        }
                    } else {
                        loading.value = false
                        error.value = task.exception?.localizedMessage
                    }
                }
        } else {
            loading.value = false
            error.value = "Fill Gaps!"
        }
    }
}