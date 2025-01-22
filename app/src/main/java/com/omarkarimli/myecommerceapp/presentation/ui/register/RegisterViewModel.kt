package com.omarkarimli.myecommerceapp.presentation.ui.register

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
class RegisterViewModel @Inject constructor(
    val sharedPreferences: SharedPreferences,
    private val provideFirestore: FirebaseFirestore,
    private val provideAuth: FirebaseAuth
): ViewModel() {

    val isNavigating: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun registerNewUser(email: String, password: String, name: String, surname: String) {
        loading.value = true

        if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && surname.isNotEmpty()) {
            // Register the user
            provideAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = provideAuth.currentUser

                        // Save additional data to Firestore
                        val userData = hashMapOf(
                            Constants.NAME to name,
                            Constants.SURNAME to surname,
                            Constants.BOOKMARKED_IDS to arrayListOf<Int>()
                        )

                        provideFirestore
                            .collection(Constants.USERS)
                            .document(user!!.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                loading.value = false
                                isNavigating.value = true
                            }
                            .addOnFailureListener { e ->
                                loading.value = false
                                error.value = e.localizedMessage
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