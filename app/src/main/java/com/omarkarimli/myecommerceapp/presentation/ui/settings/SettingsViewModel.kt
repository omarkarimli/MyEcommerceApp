package com.omarkarimli.myecommerceapp.presentation.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.omarkarimli.myecommerceapp.domain.repository.MyEcommerceRepository
import com.omarkarimli.myecommerceapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val provideRepo: MyEcommerceRepository,
    private val sharedPreferences: SharedPreferences,
    private val provideAuth: FirebaseAuth
): ViewModel() {

    val isDarkMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNoti: MutableLiveData<Boolean> = MutableLiveData(true)

    val nameSurname: MutableLiveData<String> = MutableLiveData()
    val isNavigating: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    init {
        initializeDartModeState()
        initializeNotificationState()

        fetchUserData()
    }

    private fun initializeDartModeState() {
        isDarkMode.value = sharedPreferences.getBoolean(Constants.DARK_MODE, false)
    }

    fun changeDarkModeState(isChecked: Boolean) {
        isDarkMode.value = isChecked

        sharedPreferences
            .edit()
            .putBoolean(Constants.DARK_MODE, isChecked)
            .apply()
    }

    private fun initializeNotificationState() {
        isNoti.value = sharedPreferences.getBoolean(Constants.IS_NOTI, true)
    }

    fun changeNotificationState(isChecked: Boolean) {
        isNoti.value = isChecked

        sharedPreferences
            .edit()
            .putBoolean(Constants.IS_NOTI, isChecked)
            .apply()
    }

    private fun fetchUserData() {
        loading.value = true

        viewModelScope.launch {
            try {
                val result = provideRepo.fetchUserData()

                loading.value = false

                // Fetch and display Name and Surname
                val name = result.getString("name")
                val surname = result.getString("surname")

                nameSurname.value = "$name $surname"
            } catch (e: Exception) {
                signOutAndRedirect()
            }
        }
    }

    fun signOutAndRedirect() {
        sharedPreferences.edit().clear().apply()

        provideAuth.signOut()
        error.value = "Signing out..."
        isNavigating.value = true
    }
}