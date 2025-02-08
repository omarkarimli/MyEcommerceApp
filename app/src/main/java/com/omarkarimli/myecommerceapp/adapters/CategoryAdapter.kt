package com.omarkarimli.myecommerceapp.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import com.omarkarimli.myecommerceapp.R
import com.omarkarimli.myecommerceapp.databinding.ItemCategoryBinding
import com.omarkarimli.myecommerceapp.domain.models.CategoryModel
import com.omarkarimli.myecommerceapp.utils.Constants

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    lateinit var onItemClick: (String) -> Unit

    private var selectedCategoryName = Constants.ALL
    private var categoryList = arrayListOf<CategoryModel>()

    inner class CategoryViewHolder(val binding: ItemCategoryBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CategoryViewHolder {
        val layout = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return CategoryViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryViewHolder, position: Int) {
        val categoryModel = categoryList[position]

        holder.binding.apply {
            buttonCategory.text = categoryModel.name

            // Set button background color based on selection
            val isSelected = (categoryModel.name == selectedCategoryName)

            val backgroundColor = MaterialColors.getColor(buttonCategory,
                if (isSelected)
                    com.google.android.material.R.attr.colorPrimaryContainer
                else
                    com.google.android.material.R.attr.colorOnPrimary
            )
            buttonCategory.backgroundTintList = ColorStateList.valueOf(backgroundColor)

            root.setOnClickListener {
                if (selectedCategoryName != categoryModel.name) {
                    selectedCategoryName = categoryModel.name!!
                    onItemClick(selectedCategoryName)
                    notifyDataSetChanged()
                }
            }
        }
    }

    fun updateList(newList: List<CategoryModel>) {
        categoryList.clear()
        categoryList.addAll(newList)
        notifyDataSetChanged()
    }
}