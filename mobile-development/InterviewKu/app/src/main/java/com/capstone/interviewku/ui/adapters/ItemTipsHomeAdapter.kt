package com.capstone.interviewku.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.capstone.interviewku.R
import com.capstone.interviewku.data.network.response.Article
import com.capstone.interviewku.databinding.ItemTipsHomeBinding

class ItemTipsHomeAdapter(
    private val onItemClick: (Article) -> Unit
) : ListAdapter<Article, ItemTipsHomeAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemTipsHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        holder.itemView.context.apply {
            getItem(position).let { data ->
                Glide.with(this)
                    .load(data.coverImgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.bg_no_image)
                    .placeholder(R.drawable.bg_no_image)
                    .into(binding.ivCover)

                binding.tvTitle.text = data.title

                holder.itemView.setOnClickListener {
                    onItemClick(data)
                }
            }
        }
    }

    class ViewHolder(val binding: ItemTipsHomeBinding) : RecyclerView.ViewHolder(binding.root)
}