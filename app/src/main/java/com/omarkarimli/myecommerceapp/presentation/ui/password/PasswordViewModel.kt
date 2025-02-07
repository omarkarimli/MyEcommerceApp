package com.omarkarimli.myecommerceapp.presentation.ui.password

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.myecommerceapp.R
import com.omarkarimli.myecommerceapp.domain.repository.MyEcommerceRepository
import com.omarkarimli.myecommerceapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val provideRepo: MyEcommerceRepository,
    private val sharedPreferences: SharedPreferences
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
                            success.value = R.string.password_changed.toString()
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
