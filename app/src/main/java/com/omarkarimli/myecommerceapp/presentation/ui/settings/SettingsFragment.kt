package com.omarkarimli.myecommerceapp.presentation.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarkarimli.myecommerceapp.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel by viewModels<SettingsViewModel>()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowChangePassword.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragmentToPasswordFragment()
            findNavController().navigate(action)
        }

        binding.imageViewExit.setOnClickListener {
            viewModel.signOutAndRedirect()
        }

        observeData()
    }

    private fun observeData() {
        viewModel.isNavigating.observe(viewLifecycleOwner) { isNavigating ->
            if (isNavigating) {
                val action = SettingsFragmentDirections.actionSettingsFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }

        viewModel.nameSurname.observe(viewLifecycleOwner) { nameSurname ->
            binding.textViewNameSurname.text = nameSurname
        }

    }
}