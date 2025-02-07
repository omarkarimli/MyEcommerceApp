package com.omarkarimli.myecommerceapp.presentation.ui.product

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omarkarimli.myecommerceapp.R
import com.omarkarimli.myecommerceapp.adapters.ColorAdapter
import com.omarkarimli.myecommerceapp.adapters.ImagePagerAdapter
import com.omarkarimli.myecommerceapp.databinding.FragmentProductBinding
import com.omarkarimli.myecommerceapp.utils.Constants
import com.omarkarimli.myecommerceapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.internal.wait

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private var imagePagerAdapter: ImagePagerAdapter? = null
    private val colorAdapter = ColorAdapter()

    private val args: ProductFragmentArgs by navArgs()
    private lateinit var binding: FragmentProductBinding
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Args pass to ViewModel
        viewModel.argsProductId.value = args.id
        viewModel.argsSource.value = args.source

        binding.productColorRv.adapter = colorAdapter
        binding.textViewPriceOriginal.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        binding.back.setOnClickListener { findNavController().navigateUp() }
        binding.buttonMinus.setOnClickListener { viewModel.changeNumberOfProduct(-1) }
        binding.buttonPlus.setOnClickListener { viewModel.changeNumberOfProduct(1) }
        binding.fabAddToCart.setOnClickListener {
            viewModel.toggleProductInCart(viewModel.product.value!!)
        }
        binding.buttonBookmark.setOnClickListener { viewModel.toggleBookmark(viewModel.product.value?.id!!) }

        colorAdapter.onColorClick = { viewModel.toggleColor(it) }

        observeViewModel()
    }

    private fun observeViewModel() {

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.success.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.navigatingTo.observe(viewLifecycleOwner) {
            if (it == Constants.CART) {
                val action = ProductFragmentDirections.actionProductFragmentToCartFragment()
                findNavController().navigate(action)
            }
        }

        viewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let {
                lifecycleScope.launch {
                    binding.apply {
                        productTitle.text = product.title
                        productDesc.text = product.description
                        textViewRating.text = product.averageRating.toString()
                        productSellerGender.text = "${product.seller}   |   ${product.productDetails?.gender}"

                        // Load images into ViewPager
                        if (imagePagerAdapter == null) {
                            imagePagerAdapter = ImagePagerAdapter(product.images)
                            viewPager2.adapter = imagePagerAdapter
                            dotsIndicator.attachTo(viewPager2)
                        }

                        // Update button state based on isBookmarked
                        buttonBookmark.icon = ContextCompat.getDrawable(
                            buttonBookmark.context,
                            if (product.isBookmarked) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
                        )

                        if (args.source == Constants.CART) {
                            viewModel.numberOfProduct.value = product.numberOfProduct

                            viewModel.updateProductLocally(product)

                            fabAddToCart.text = if (product.isCarted) "Remove from cart" else "Add to cart"
                            fabAddToCart.backgroundTintList = ContextCompat.getColorStateList(
                                requireContext(),
                                if (product.isCarted) R.color.secondary_container else R.color.on_tertiary_container
                            )
                            fabAddToCart.setTextColor(ContextCompat.getColor(fabAddToCart.context,
                                if (product.isCarted) R.color.on_tertiary_container else R.color.white
                            ))
                        }
                    }
                }
            }
        }

        viewModel.colorModels.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                colorAdapter.updateList(it)
                binding.containerColor.visibleItem()
            }
        }

        viewModel.numberOfProduct.observe(viewLifecycleOwner) { n ->
            binding.textViewNumberOfProduct.text = "$n"

            binding.buttonMinus.isEnabled = n > 1
        }

        viewModel.totalOriginalPrice.observe(viewLifecycleOwner) {
            binding.textViewPriceOriginal.text = "$$it"
        }

        viewModel.totalDiscountedPrice.observe(viewLifecycleOwner) {
            binding.textViewPriceDiscounted.text = "$$it"
        }
    }
}
