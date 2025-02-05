package com.omarkarimli.myecommerceapp.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.myecommerceapp.databinding.ItemColorBinding
import com.omarkarimli.myecommerceapp.domain.models.ColorModel
import com.omarkarimli.myecommerceapp.utils.goneItem
import com.omarkarimli.myecommerceapp.utils.visibleItem

class ColorAdapter : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    lateinit var onColorClick: (ColorModel) -> Unit

    private var list = arrayListOf<ColorModel>()

    inner class ColorViewHolder(val binding: ItemColorBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val layout = ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorViewHolder(layout)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val colorModel = list[position]
        holder.binding.imageViewMain.setColorFilter(colorModel.colorString!!.toColorInt(), PorterDuff.Mode.SRC_IN)

        if (colorModel.isSelected) {
            holder.binding.imageViewSelected.visibleItem()
        } else {
            holder.binding.imageViewSelected.goneItem()
        }

        holder.binding.root.setOnClickListener {
            onColorClick(colorModel)
        }
    }

    fun updateList(newList: List<ColorModel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}
