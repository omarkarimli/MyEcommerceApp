package com.omarkarimli.myecommerceapp.presentation.ui.product

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omarkarimli.myecommerceapp.R
import com.omarkarimli.myecommerceapp.adapters.ColorAdapter
import com.omarkarimli.myecommerceapp.adapters.ImagePagerAdapter
import com.omarkarimli.myecommerceapp.databinding.FragmentProductBinding
import com.omarkarimli.myecommerceapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment() {

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

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonMinus.setOnClickListener {
            viewModel.changeNumberOfProduct(-1)
        }
        binding.buttonPlus.setOnClickListener {
            viewModel.changeNumberOfProduct(1)
        }

        binding.fabAddToCart.setOnClickListener {
            viewModel.addProductToCart(viewModel.product.value!!)
        }

        colorAdapter.onColorClick = {
            viewModel.toggleColor(it)
        }

        binding.buttonBookmark.setOnClickListener {
            viewModel.toggleBookmark(args.id)
        }

        viewModel.productId.value = args.id

        binding.productColorRv.adapter = colorAdapter

        binding.textViewPriceOriginal.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let {
                binding.apply {
                    productTitle.text = product.title
                    productDesc.text = product.description
                    textViewRating.text = product.averageRating.toString()
                    productSellerGender.text = "${product.seller}   |   ${product.productDetails?.gender}"

                    // Load images into ViewPager
                    val imageUrls = product.images
                    val adapter = ImagePagerAdapter(imageUrls)
                    viewPager2.adapter = adapter

                    // Set up dots indicator
                    dotsIndicator.attachTo(viewPager2)

                    // Update button state based on isBookmarked
                    buttonBookmark.icon = ContextCompat.getDrawable(
                        buttonBookmark.context,
                        if (product.isBookmarked) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
                    )

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

        viewModel.colorModels.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                colorAdapter.updateList(it)
                binding.containerColor.visibleItem()
            }
        }

        viewModel.numberOfProduct.observe(viewLifecycleOwner) {
            binding.textViewNumberOfProduct.text = "$it"
        }

        viewModel.totalOriginalPrice.observe(viewLifecycleOwner) {
            binding.textViewPriceOriginal.text = "$$it"
        }

        viewModel.totalDiscountedPrice.observe(viewLifecycleOwner) {
            binding.textViewPriceDiscounted.text = "$$it"
        }
    }
}
