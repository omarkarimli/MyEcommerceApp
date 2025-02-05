package com.omarkarimli.myecommerceapp.presentation.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnBoardingViewModel : ViewModel() {

    val isFabVisible = MutableLiveData(false)

    fun updateFabVisibility(currentItem: Int, itemCount: Int) {
        isFabVisible.value = currentItem == itemCount - 1
    }
}