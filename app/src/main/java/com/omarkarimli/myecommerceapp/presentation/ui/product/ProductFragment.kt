package com.omarkarimli.myecommerceapp.presentation.ui.product

import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
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
import com.omarkarimli.myecommerceapp.adapters.ImagePagerAdapter
import com.omarkarimli.myecommerceapp.databinding.FragmentProductBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment() {

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

        val args: ProductFragmentArgs by navArgs()
        viewModel.getProductById(args.id)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let {
                binding.apply {
                    productTitle.text = it.title
                    productDesc.text = it.description
                    textViewRate.text = it.averageRating.toString()

                    val discount = product.discount ?: 0
                    if (product.discount != null) {
                        textViewDiscount.text = "-${product.discount}%"

                        val priceDiscounted = product.originalPrice!! - (product.originalPrice * discount) / 100

                        // Create Spannable for discounted price
                        val discountedPriceText = SpannableString("$${priceDiscounted} ")
                        discountedPriceText.setSpan(
                            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.red)), // Set color red
                            0,
                            discountedPriceText.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        discountedPriceText.setSpan(
                            RelativeSizeSpan(1.2f), // Make discounted price 20% larger
                            0,
                            discountedPriceText.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )

                        // Create Spannable for original price
                        val originalPriceText = SpannableString("$${product.originalPrice}")
                        originalPriceText.setSpan(
                            StrikethroughSpan(),
                            0,
                            originalPriceText.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        originalPriceText.setSpan(
                            RelativeSizeSpan(1f), // Make original price 20% smaller
                            0,
                            originalPriceText.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )

                        // Combine both parts into a SpannableStringBuilder
                        val finalPriceText = SpannableStringBuilder()
                        finalPriceText.append(discountedPriceText)
                        finalPriceText.append(originalPriceText)

                        textViewPrice.text = finalPriceText
                    }

                    // Load images into ViewPager
                    val imageUrls = product.images
                    val adapter = ImagePagerAdapter(imageUrls)
                    viewPager2.adapter = adapter

                    // Set up dots indicator
                    dotsIndicator.attachTo(viewPager2)
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
    }
}
