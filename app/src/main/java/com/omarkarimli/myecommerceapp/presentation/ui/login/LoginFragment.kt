package com.omarkarimli.myecommerceapp.presentation.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarkarimli.myecommerceapp.databinding.FragmentLoginBinding
import com.omarkarimli.myecommerceapp.utils.goneItem
import com.omarkarimli.myecommerceapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.signUpLink.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        binding.signInButton.setOnClickListener {
            val rememberMe = binding.rememberMe.isChecked
            val email = binding.editTextEmail.text.toString().trim() + "@gmail.com"
            val password = binding.editTextPassword.text.toString().trim()

            viewModel.loginUserAccount(
                rememberMe,
                email,
                password
            )
        }

        observeData()
    }

    private fun observeData() {
        viewModel.isNavigating.observe(viewLifecycleOwner) { isNavigating ->
            if (isNavigating) {
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.progressBar.visibleItem()
            } else {
                binding.progressBar.goneItem()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }
    }
}
