package com.omarkarimli.myecommerceapp.presentation.ui.splash

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.omarkarimli.myecommerceapp.databinding.FragmentSplashBinding
import com.omarkarimli.myecommerceapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var action: NavDirections? = null

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Using LifecycleScope to delay and navigate
        lifecycleScope.launch {
            // Wait for 2 seconds
            delay(2000)

            // Check login status
            val isLogged = sharedPreferences.getBoolean(Constants.IS_LOGGED_KEY, false)

            action = if (isLogged) {
                // Navigate to Home
                SplashFragmentDirections.actionSplashFragmentToHomeFragment()
            } else {
                // Navigate to OnBoarding
                SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment()
            }

            findNavController().navigate(action!!)
        }
    }
}