package com.omarkarimli.myecommerceapp.presentation.ui.password

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.omarkarimli.myecommerceapp.R
import com.omarkarimli.myecommerceapp.domain.repository.MyEcommerceRepository
import com.omarkarimli.myecommerceapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val provideRepo: MyEcommerceRepository,
    private val provideAuth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val isNavigating: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()
    val success: MutableLiveData<String> = MutableLiveData()

    fun changePassword(email: String, currentPassword: String, newPassword: String, confirmNewPassword: String) {
        // Validate input fields
        if (currentPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmNewPassword.isNotEmpty()) {
            if (newPassword != currentPassword) {
                if (newPassword == confirmNewPassword) {
                    viewModelScope.launch {
                        try {
                            provideRepo.changePassword(email, currentPassword, newPassword)

                            success.value = "Password changed successfully"
                            isNavigating.value = true
                        } catch (e: Exception) {
                            error.value = e.message ?: "An error occurred"
                        }
                    }
                } else {
                    error.value = "New passwords does not match"
                }
            } else {
                error.value = "New password should not same as current ones"
            }
        } else {
            error.value = "Fill Gaps"
        }
    }

    fun sendPasswordChangedNotification(applicationContext: Context) {
        val isNoti = sharedPreferences.getBoolean(Constants.IS_NOTI, true)

        if (isNoti) {
            val channelId = "password_change_channel"
            val notificationId = 1

            // Create Notification Manager
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Create Notification Channel (for Android 8.0+)
            val channel = NotificationChannel(
                channelId,
                "Password Change Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies when the password is changed"
            }
            notificationManager.createNotificationChannel(channel)

            // Build Notification
            val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Password Changed")
                .setContentText("Your password has been successfully updated.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            // Show Notification
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }
}
