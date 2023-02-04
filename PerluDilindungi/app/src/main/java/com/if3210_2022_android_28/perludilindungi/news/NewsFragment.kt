package com.if3210_2022_android_28.perludilindungi.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.if3210_2022_android_28.perludilindungi.databinding.FragmentNewsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding

    private val newsViewModel: NewsViewModel by lazy {
        ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    /**
     * Inflates the fragment_news layout using View Binding,
     * sets the lifecycleOnwer to itself,
     * binds the ViewModel,
     * sets up the RecyclerView with a NewsFragmentAdapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.newsViewModel = this.newsViewModel
        binding.fragmentNewsRecycler.adapter =
            NewsFragmentAdapter(NewsFragmentAdapter.NewsListOnClickListener {
                this.newsViewModel.navigateToSelectedNews(it)
            })
        this.newsViewModel.selectedNews.observe(this.viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(
                    NewsFragmentDirections.actionNewsFragmentToNewsWebFragment(it)
                )
                this.newsViewModel.navigateToSelectedNewsComplete()
            }
        })
        
        return binding.root
    }
}