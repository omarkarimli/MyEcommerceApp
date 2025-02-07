package com.omarkarimli.myecommerceapp.presentation

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.omarkarimli.myecommerceapp.R
import com.omarkarimli.myecommerceapp.databinding.ActivityMainBinding
import com.omarkarimli.myecommerceapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavigation()
        setTheme()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomMain, navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment, R.id.onBoardingFragment, R.id.loginFragment, R.id.registerFragment, R.id.productFragment, R.id.passwordFragment, R.id.bookmarkFragment -> {
                    binding.bottomMain.visibility = View.GONE
                }

                else -> binding.bottomMain.visibility = View.VISIBLE
            }
        }
    }

    private fun setTheme() {
        val isDarkMode = sharedPreferences.getBoolean(Constants.DARK_MODE, false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}