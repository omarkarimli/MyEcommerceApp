package com.omarkarimli.myecommerceapp.presentation.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarkarimli.myecommerceapp.adapters.CategoryAdapter
import com.omarkarimli.myecommerceapp.adapters.ProductAdapter
import com.omarkarimli.myecommerceapp.databinding.FragmentBookmarkBinding
import com.omarkarimli.myecommerceapp.utils.goneItem
import com.omarkarimli.myecommerceapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment: Fragment() {

    private val viewModel by viewModels<BookmarkViewModel>()

    private var productAdapter = ProductAdapter()
    private var categoryAdapter = CategoryAdapter()

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        // Refresh bookmark data when returning to HomeFragment
        viewModel.fetchBookmarkedIds()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvProducts.adapter = productAdapter
        binding.rvCategories.adapter = categoryAdapter

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        productAdapter.onItemClick = {
            val action = BookmarkFragmentDirections.actionBookmarkFragmentToProductFragment(it.id!!)
            findNavController().navigate(action)
        }

        productAdapter.onBookmarkClick = {
            viewModel.toggleBookmark(it)
        }

        categoryAdapter.onItemClick = {
            viewModel.filterProductsByCategory(it)
        }

        observeData()
    }

    private fun observeData() {

        viewModel.filteredProducts.observe(viewLifecycleOwner) {
            productAdapter.updateProductList(it)

            if (it.isEmpty()) {
                binding.rvProducts.goneItem()
                binding.containerState.visibleItem()
            } else {
                binding.rvProducts.visibleItem()
                binding.containerState.goneItem()
            }
        }

        viewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter.updateList(it)
        }

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
    }
}