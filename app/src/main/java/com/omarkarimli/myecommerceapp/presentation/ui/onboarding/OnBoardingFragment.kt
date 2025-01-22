package com.omarkarimli.myecommerceapp.presentation.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.omarkarimli.myecommerceapp.adapters.ViewPagerAdapter
import com.omarkarimli.myecommerceapp.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {

    private val viewModel by viewModels<OnBoardingViewModel>()

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentList = arrayListOf(
            FirstOnBoardingFragment(),
            SecondOnBoardingFragment(),
            ThirdOnBoardingFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager2.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager2)

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.updateFabVisibility(position, adapter.itemCount)
            }
        })

        binding.fabNext.setOnClickListener {
            viewModel.isNavigating.value = true
        }

        observeData()
    }

    private fun observeData() {
        viewModel.isFabVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.fabNext.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        viewModel.isNavigating.observe(viewLifecycleOwner) { isNavigating ->
            if (isNavigating) {
                val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }
    }
}