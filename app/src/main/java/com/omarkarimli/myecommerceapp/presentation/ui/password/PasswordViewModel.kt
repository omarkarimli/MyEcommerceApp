package com.omarkarimli.myecommerceapp.presentation.ui.password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.myecommerceapp.domain.repository.MyEcommerceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val provideRepo: MyEcommerceRepository
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
                    viewModelScope.launch {
                        try {
                            provideRepo.changePassword(currentPassword)

                            isNavigating.value = true
                            success.value = "Password updated successfully"
                        } catch (e: Exception) {
                            error.value = "Error updating password: ${e.message}"
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