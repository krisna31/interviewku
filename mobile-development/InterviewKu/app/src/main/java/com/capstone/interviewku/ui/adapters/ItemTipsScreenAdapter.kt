package com.capstone.interviewku.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.capstone.interviewku.R
import com.capstone.interviewku.data.room.entity.ArticleEntity
import com.capstone.interviewku.databinding.ItemTipsScreenBinding

class ItemTipsScreenAdapter(
    private val onItemClick: (ArticleEntity) -> Unit
) : PagingDataAdapter<ArticleEntity, ItemTipsScreenAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<ArticleEntity>() {
        override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean =
            oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemTipsScreenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        holder.itemView.context.apply {
            getItem(position)?.let { data ->
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

    class ViewHolder(val binding: ItemTipsScreenBinding) : RecyclerView.ViewHolder(binding.root)
}