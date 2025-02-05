package com.omarkarimli.myecommerceapp.presentation.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.omarkarimli.myecommerceapp.R
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

        binding.containerBookmark.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragmentToBookmarkFragment()
            findNavController().navigate(action)
        }

        binding.containerChangePassword.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragmentToPasswordFragment()
            findNavController().navigate(action)
        }

        binding.imageViewExit.setOnClickListener {
            buildAlertDialog(requireContext())
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

    private fun buildAlertDialog(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Are you sure?")
            .setMessage("You will sign out and redirect to login screen")
            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to neutral button press
            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                viewModel.signOutAndRedirect()
            }
            .show()
    }
}