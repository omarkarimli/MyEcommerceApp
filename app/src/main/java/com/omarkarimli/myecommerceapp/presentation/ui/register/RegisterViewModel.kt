package com.omarkarimli.myecommerceapp.presentation.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.myecommerceapp.domain.repository.AuthRepository
import com.omarkarimli.myecommerceapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val provideRepo: AuthRepository
): ViewModel() {

    val isNavigating: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun registerNewUser(email: String, password: String, name: String, surname: String) {
        loading.value = true

        if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && surname.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    provideRepo.registerNewUser(email, password)

                    // Save additional data to Firestore
                    viewModelScope.launch {
                        try {
                            val userData = hashMapOf(
                                Constants.NAME to name,
                                Constants.SURNAME to surname,
                                Constants.BOOKMARKED_IDS to arrayListOf<Int>()
                            )
                            provideRepo.addUserToFirestore(userData)

                            loading.value = false
                            isNavigating.value = true
                        } catch (e: Exception) {
                            loading.value = false
                            error.value = e.localizedMessage
                        }
                    }
                } catch (e: Exception) {
                    loading.value = false
                    error.value = e.localizedMessage
                }
            }
        } else {
            loading.value = false
            error.value = "Fill Gaps!"
        }
    }

}