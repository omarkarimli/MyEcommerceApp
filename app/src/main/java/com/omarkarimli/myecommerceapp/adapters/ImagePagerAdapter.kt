package com.omarkarimli.myecommerceapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
                .placeholder(R.drawable.no_picture)
                .error(R.drawable.baseline_error_outline_24)
                .into(imageView)
        }
    }
}
