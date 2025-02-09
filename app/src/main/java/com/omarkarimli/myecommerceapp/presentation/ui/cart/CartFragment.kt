package com.omarkarimli.myecommerceapp.presentation.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarkarimli.myecommerceapp.adapters.CartProductAdapter
import com.omarkarimli.myecommerceapp.databinding.FragmentCartBinding
import com.omarkarimli.myecommerceapp.utils.Constants
import com.omarkarimli.myecommerceapp.utils.goneItem
import com.omarkarimli.myecommerceapp.utils.roundDouble
import com.omarkarimli.myecommerceapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    val adapter = CartProductAdapter()

    private val viewModel by viewModels<CartViewModel>()

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCartProducts.adapter = adapter

        adapter.onItemClick = {
            val action = CartFragmentDirections.actionCartFragmentToProductFragment(it.localId!!, Constants.CART)
            findNavController().navigate(action)
        }
        adapter.onNumberOfProductClick = { product, n -> viewModel.changeNumberOfProduct(product, n) }
        adapter.onDeleteClick = { viewModel.deleteProduct(it) }

        observeData()
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchCartProducts()
    }

    private fun observeData() {
        viewModel.cartProductList.observe(viewLifecycleOwner) {
            viewModel.calculateTotalCartPrice(it)

            if (it.isEmpty()) {
                binding.rvCartProducts.goneItem()
                binding.containerState.visibleItem()
            } else {
                adapter.updateCartProductList(it)

                binding.rvCartProducts.visibleItem()
                binding.containerState.goneItem()
            }
        }

        viewModel.totalCartPrice.observe(viewLifecycleOwner) {
            binding.textViewTotalCartPrice.text = "$${roundDouble(it)}"
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibleItem()
                binding.rvCartProducts.goneItem()
            } else {
                binding.progressBar.goneItem()
                binding.rvCartProducts.visibleItem()
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