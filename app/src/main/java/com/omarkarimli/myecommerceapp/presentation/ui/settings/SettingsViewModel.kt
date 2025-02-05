package com.omarkarimli.myecommerceapp.presentation.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.omarkarimli.myecommerceapp.domain.repository.MyEcommerceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val provideRepo: MyEcommerceRepository,
    private val sharedPreferences: SharedPreferences,
    private val provideAuth: FirebaseAuth
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