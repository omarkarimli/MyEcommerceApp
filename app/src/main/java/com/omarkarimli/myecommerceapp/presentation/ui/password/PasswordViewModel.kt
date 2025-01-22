package com.omarkarimli.myecommerceapp.presentation.ui.password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class PasswordViewModel @Inject constructor(
    private val provideCurrentUser: FirebaseUser?
) : ViewModel() {

    val isNavigating: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()
    val success: MutableLiveData<String> = MutableLiveData()

    fun changePassword(currentPassword: String, newPassword: String, confirmNewPassword: String) {

        // Validate input fields
        if (currentPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmNewPassword.isNotEmpty()) {
            if (newPassword != currentPassword) {
                if (newPassword == confirmNewPassword) {
                    val credential = EmailAuthProvider.getCredential(provideCurrentUser!!.email!!, currentPassword)

                    provideCurrentUser
                        .reauthenticate(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                provideCurrentUser
                                    .updatePassword(newPassword)
                                    .addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        isNavigating.value = true
                                        success.value = "Password updated successfully"
                                    } else {
                                        error.value = "Failed to update password: ${updateTask.exception?.message}"
                                    }
                                }
                            } else {
                                error.value = "Failed to reauthenticate user: ${task.exception?.message}"
                            }
                    }
                } else {
                    error.value = "New passwords do not match"
                }
            } else {
                error.value = "New password cannot be the same as the current password"
            }
        } else {
            error.value = "Fill Gaps!"
        }
    }
}