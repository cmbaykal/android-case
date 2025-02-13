package com.mrbaikal.nesineandroidcase.ui.views.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.transformations
import coil3.transform.CircleCropTransformation
import coil3.transform.Transformation
import com.mrbaikal.nesineandroidcase.databinding.ItemviewPostBinding
import com.mrbaikal.nesineandroidcase.domain.model.PostModel

class PostAdapter(
    private val onClick: (PostModel) -> Unit
) : ListAdapter<PostModel, PostAdapter.PostViewHolder>(PostDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemviewPostBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item, position)
    }

    inner class PostViewHolder(private val binding: ItemviewPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostModel, position: Int) {
            val imgUrl = "https://picsum.photos/300/300?random=$position&grayscale"
            binding.imagePost.load(imgUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
            binding.textTitle.text = item.title
            binding.textBody.text = item.body
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    class PostDiffUtil : DiffUtil.ItemCallback<PostModel>() {
        override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.body == newItem.body
        }
    }
}