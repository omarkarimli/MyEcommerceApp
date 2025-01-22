package com.omarkarimli.myecommerceapp.presentation.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnBoardingViewModel : ViewModel() {

    private val _isFabVisible = MutableLiveData(false)
    val isFabVisible: LiveData<Boolean> get() = _isFabVisible

    val isNavigating: MutableLiveData<Boolean> = MutableLiveData(false)

    fun updateFabVisibility(currentItem: Int, itemCount: Int) {
        _isFabVisible.value = currentItem == itemCount - 1
    }
}