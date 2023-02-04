package com.if3210_2022_android_28.perludilindungi.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.if3210_2022_android_28.perludilindungi.databinding.FragmentNewsListBinding
import com.if3210_2022_android_28.perludilindungi.model.NewsResponse

class NewsFragmentAdapter(private val newsListOnClickListener: NewsListOnClickListener) :
    ListAdapter<NewsResponse.News, NewsFragmentAdapter.NewsFragmentViewHolder>(DiffCallback) {

    class NewsFragmentViewHolder(private var binding: FragmentNewsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsResponse.News) {
            this.binding.news = news
            this.binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFragmentViewHolder {
        return NewsFragmentViewHolder(FragmentNewsListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: NewsFragmentViewHolder, position: Int) {
        val news = getItem(position)
        holder.itemView.setOnClickListener {
            this.newsListOnClickListener.onClick(news)
        }
        holder.bind(news)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<NewsResponse.News>() {
        override fun areItemsTheSame(
            oldItem: NewsResponse.News,
            newItem: NewsResponse.News
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: NewsResponse.News,
            newItem: NewsResponse.News
        ): Boolean {
            return oldItem.guid == newItem.guid
        }
    }

    class NewsListOnClickListener(val clickListener: (news: NewsResponse.News) -> Unit) {
        fun onClick(news: NewsResponse.News) = clickListener(news)
    }
}