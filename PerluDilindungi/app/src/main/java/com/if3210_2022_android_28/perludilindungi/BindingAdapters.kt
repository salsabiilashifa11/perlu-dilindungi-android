package com.if3210_2022_android_28.perludilindungi

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.if3210_2022_android_28.perludilindungi.model.NewsResponse
import com.if3210_2022_android_28.perludilindungi.news.NewsApiStatus
import com.if3210_2022_android_28.perludilindungi.news.NewsFragmentAdapter

@BindingAdapter("enclosureUrl")
fun bindNewsImage(imageView: ImageView, enclosureUrl: String?) {
    enclosureUrl?.let {
        val imageUri = enclosureUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context).load(imageUri).apply(
            RequestOptions().placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
        ).into(imageView)
    }
}

@BindingAdapter("newsList")
fun bindNewsRecyclerView(recyclerView: RecyclerView, list: List<NewsResponse.News>?) {
    val adapter = recyclerView.adapter as NewsFragmentAdapter
    adapter.submitList(list)
}

@BindingAdapter("newsApiStatus")
fun bindNewsStatus(imageView: ImageView, status: NewsApiStatus?) {
    when (status) {
        NewsApiStatus.LOADING -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.ic_baseline_autorenew_24_animation)
        }
        NewsApiStatus.ERROR -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.ic_baseline_cloud_off_24)
        }
        else -> {
            imageView.visibility = View.GONE
        }
    }
}