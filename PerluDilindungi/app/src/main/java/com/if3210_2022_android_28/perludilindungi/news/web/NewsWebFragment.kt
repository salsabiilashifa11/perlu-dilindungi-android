package com.if3210_2022_android_28.perludilindungi.news.web

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.if3210_2022_android_28.perludilindungi.databinding.FragmentNewsWebBinding

/**
 * A simple [Fragment] subclass.
 * Use the [NewsWebFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsWebFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        val binding = FragmentNewsWebBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner
        val news = NewsWebFragmentArgs.fromBundle(requireArguments()).selectedNews
        val newsWebViewModelFactory = NewsWebViewModelFactory(news, application)
        binding.newsWebViewModel =
            ViewModelProvider(this, newsWebViewModelFactory)[NewsWebViewModel::class.java]
        binding.fragmentNewsWebWeb.loadUrl(news.guid)
        return binding.root
    }
}