package com.omarkarimli.myecommerceapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.myecommerceapp.R
import com.omarkarimli.myecommerceapp.databinding.ItemImagePagerBinding
import com.squareup.picasso.Picasso

class ImagePagerAdapter(private val imageUrls: ArrayList<String?>?) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemImagePagerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layout = ItemImagePagerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return imageUrls?.size ?: 0
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls?.get(position)

        holder.binding.apply {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.baseline_hide_image_24)
                .error(R.drawable.baseline_hide_image_24)
                .into(imageView)
        }
    }
}
