package com.omarkarimli.myecommerceapp.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarkarimli.myecommerceapp.adapters.CategoryAdapter
import com.omarkarimli.myecommerceapp.adapters.ProductAdapter
import com.omarkarimli.myecommerceapp.databinding.FragmentHomeBinding
import com.omarkarimli.myecommerceapp.utils.Constants
import com.omarkarimli.myecommerceapp.utils.goneItem
import com.omarkarimli.myecommerceapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private val viewModel by viewModels<HomeViewModel>()

    private var productAdapter = ProductAdapter()
    private var categoryAdapter = CategoryAdapter()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchBookmarkedIds()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvProducts.adapter = productAdapter
        binding.rvCategories.adapter = categoryAdapter

        productAdapter.onItemClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToProductFragment(it.id!!, Constants.HOME)
            findNavController().navigate(action)
        }
        productAdapter.onBookmarkClick = { viewModel.toggleBookmark(it) }
        categoryAdapter.onItemClick = { viewModel.filterProductsByCategory(it) }

        binding.editTextSearch.doOnTextChanged { inputText, _, _, _ ->
            if (inputText.toString().isNotEmpty()) {
                val searchQuery = inputText.toString().trim()
                val searchedProducts = viewModel.filteredProducts.value?.filter { product ->
                    product.title?.contains(searchQuery, ignoreCase = true) == true
                }
                productAdapter.updateProductList(searchedProducts ?: emptyList())

                if (searchedProducts.isNullOrEmpty()) {
                    binding.containerState.visibleItem()
                    binding.rvProducts.goneItem()
                } else {
                    binding.containerState.goneItem()
                    binding.rvProducts.visibleItem()
                }
            }
        }

        observeData()
    }

    private fun observeData() {

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibleItem()
                binding.rvProducts.goneItem()
                binding.rvCategories.goneItem()
            } else {
                binding.progressBar.goneItem()
                binding.rvProducts.visibleItem()
                binding.rvCategories.visibleItem()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.success.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.filteredProducts.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.rvProducts.goneItem()
                binding.containerState.visibleItem()
            } else {
                productAdapter.updateProductList(it)

                binding.rvProducts.visibleItem()
                binding.containerState.goneItem()
            }
        }

        viewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter.updateList(it)
        }
    }
}