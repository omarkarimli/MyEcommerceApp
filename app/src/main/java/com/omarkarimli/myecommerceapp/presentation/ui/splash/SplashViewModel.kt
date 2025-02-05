package com.omarkarimli.myecommerceapp.presentation.ui.splash

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.myecommerceapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    val isLogged: MutableLiveData<Boolean> = MutableLiveData()

    init {
        viewModelScope.launch {
            // Wait for 2 seconds
            delay(2000)

            // Check login status
            isLogged.value = sharedPreferences.getBoolean(Constants.IS_LOGGED_KEY, false)
        }
    }
}