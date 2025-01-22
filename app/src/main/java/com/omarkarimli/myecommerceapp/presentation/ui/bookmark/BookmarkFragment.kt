package com.omarkarimli.myecommerceapp.presentation.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarkarimli.myecommerceapp.adapters.ProductAdapter
import com.omarkarimli.myecommerceapp.databinding.FragmentBookmarkBinding
import com.omarkarimli.myecommerceapp.databinding.FragmentHomeBinding
import com.omarkarimli.myecommerceapp.utils.goneItem
import com.omarkarimli.myecommerceapp.utils.visibleItem

class BookmarkFragment : Fragment() {

    private val viewModel by viewModels<BookmarkViewModel>()

    private var productAdapter = ProductAdapter()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvProducts.adapter = productAdapter

        observeData()

        productAdapter.onItemClick = {
            val action = BookmarkFragmentDirections.actionBookmarkFragmentToProductFragment(it.id!!)
            findNavController().navigate(action)
        }
    }

    private fun observeData() {
        viewModel.productList.observe(viewLifecycleOwner) { productData ->
            productAdapter.updateProductList(productData)
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.progressBar.visibleItem()
                binding.rvProducts.goneItem()
            } else {
                binding.progressBar.goneItem()
                binding.rvProducts.visibleItem()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }
    }
}