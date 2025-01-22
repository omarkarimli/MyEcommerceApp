package com.omarkarimli.myecommerceapp.presentation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omarkarimli.myecommerceapp.R
import com.omarkarimli.myecommerceapp.adapters.CategoryAdapter
import com.omarkarimli.myecommerceapp.adapters.ProductAdapter
import com.omarkarimli.myecommerceapp.databinding.FragmentHomeBinding
import com.omarkarimli.myecommerceapp.models.ProductModel
import com.omarkarimli.myecommerceapp.utils.goneItem
import com.omarkarimli.myecommerceapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fab New Product
        binding.fabAdd.setOnClickListener {
            writeCategoriesToFirestore()
        }

        binding.rvProducts.adapter = productAdapter
        binding.rvCategories.adapter = categoryAdapter

        productAdapter.onItemClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToProductFragment(it.id!!)
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
        }

        viewModel.bookmarkedIds.observe(viewLifecycleOwner) {
            productAdapter.updateBookmarkedProductsId(it)
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

    private fun writeProductsToFirestore() {
        val firestore = FirebaseFirestore.getInstance()

        // Step 1: Read the JSON file from the raw resource
        val inputStream = resources.openRawResource(R.raw.products)
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        // Step 2: Parse the JSON string into a list of maps or objects
        val gson = Gson()
        val productListType = object : TypeToken<List<Map<String, Any>>>() {}.type
        val products: List<Map<String, Any>> = gson.fromJson(jsonString, productListType)

        // Step 3: Add each product to Firestore
        for (product in products) {
            firestore.collection("products")
                .add(product)
                .addOnSuccessListener {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    Log.d("111", "Success")
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
                    Log.d("111", it.message.toString())
                }
        }
    }

    private fun writeCategoriesToFirestore() {
        val firestore = FirebaseFirestore.getInstance()

        // Step 1: Read the JSON file from the raw resource
        val inputStream = resources.openRawResource(R.raw.categories)
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        // Step 2: Parse the JSON string into a list of maps or objects
        val gson = Gson()
        val categoriesListType = object : TypeToken<List<Map<String, Any>>>() {}.type
        val categories: List<Map<String, Any>> = gson.fromJson(jsonString, categoriesListType)

        // Step 3: Add each category to Firestore
        for (category in categories) {
            firestore.collection("categories")
                .add(category)
                .addOnSuccessListener {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    Log.d("111", "Success")
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
                    Log.d("111", it.message.toString())
                }
        }
    }
}