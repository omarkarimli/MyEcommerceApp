package com.omarkarimli.myecommerceapp.presentation.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.omarkarimli.myecommerceapp.R
import com.omarkarimli.myecommerceapp.databinding.FragmentSettingsBinding
import com.omarkarimli.myecommerceapp.utils.Constants
import com.omarkarimli.myecommerceapp.utils.goneItem
import com.omarkarimli.myecommerceapp.utils.visibleItem
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

        binding.cardViewExit.setOnClickListener {
            buildAlertDialog(requireContext())
        }

        binding.switchNotifications.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.changeNotificationState(isChecked)
        }

        binding.switchDarkMode.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.changeDarkModeState(isChecked)
        }

        observeData()
    }

    private fun observeData() {

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibleItem()
            } else {
                binding.progressBar.goneItem()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isNavigating.observe(viewLifecycleOwner) { isNavigating ->
            if (isNavigating) {
                val action = SettingsFragmentDirections.actionSettingsFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }

        viewModel.nameSurname.observe(viewLifecycleOwner) { nameSurname ->
            binding.textViewNameSurname.text = nameSurname
        }

        viewModel.isNoti.observe(viewLifecycleOwner) {
            binding.switchNotifications.isChecked = it

            binding.switchNotifications.thumbIconDrawable = ContextCompat.getDrawable(
                binding.switchNotifications.context,
                if (it) R.drawable.baseline_done_24 else R.drawable.baseline_close_24
            )

            val trackColor = ContextCompat.getColorStateList(
                requireContext(),
                if (it) R.color.green else R.color.gray_tone_2
            )
            binding.switchNotifications.trackTintList = trackColor
        }

        viewModel.isDarkMode.observe(viewLifecycleOwner) {
            binding.switchDarkMode.isChecked = it

            binding.switchDarkMode.thumbIconDrawable = ContextCompat.getDrawable(
                binding.switchDarkMode.context,
                if (it) R.drawable.baseline_done_24 else R.drawable.baseline_close_24
            )

            val trackColor = ContextCompat.getColorStateList(
                requireContext(),
                if (it) R.color.green else R.color.gray_tone_2
            )
            binding.switchDarkMode.trackTintList = trackColor

            // Apply theme
            AppCompatDelegate.setDefaultNightMode(
                if (it) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }


    private fun buildAlertDialog(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Are you sure?")
            .setMessage("You will sign out and redirect to login screen")
            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to neutral button press
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                viewModel.signOutAndRedirect()
            }
            .show()
    }
}