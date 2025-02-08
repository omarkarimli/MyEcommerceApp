package com.omarkarimli.myecommerceapp.presentation.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.myecommerceapp.domain.repository.AuthRepository
import com.omarkarimli.myecommerceapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val provideRepo: AuthRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    val isNavigating: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun loginUserAccount(isChecked: Boolean, email: String, password: String) {
        loading.value = true

        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val result = provideRepo.loginUserAccount(isChecked, email, password)

                    // Check if user exist
                    if (result.user != null) {
                        loading.value = false

                        sharedPreferences
                            .edit()
                            .putBoolean(Constants.IS_LOGGED_KEY, isChecked)
                            .putBoolean(Constants.IS_NOTI, true)
                            .putBoolean(Constants.DARK_MODE, false)
                            .apply()

                        // Navigate to the home screen
                        isNavigating.value = true
                    }
                } catch (e: Exception) {
                    loading.value = false
                    error.value = e.message
                    Log.e("555", "Error: ${e.message}")
                }
            }
        } else {
            loading.value = false
            error.value = "Fill Gaps!"
        }
    }
}