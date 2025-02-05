package com.omarkarimli.myecommerceapp.presentation.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.omarkarimli.myecommerceapp.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment: Fragment() {

    private var action: NavDirections? = null

    private val viewModel by viewModels<SplashViewModel>()

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

        observeData()
    }

    private fun observeData() {
        viewModel.isLogged.observe(viewLifecycleOwner) {
            action = if (it == true) {
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