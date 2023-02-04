package com.if3210_2022_android_28.perludilindungi.faskes.fragments

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.if3210_2022_android_28.perludilindungi.model.Faskes
import com.if3210_2022_android_28.perludilindungi.room.FaskesDB
import com.if3210_2022_android_28.perludilindungi.faskes.DaftarFaskesViewModel
import com.if3210_2022_android_28.perludilindungi.databinding.FaskesFragmentBinding
import com.if3210_2022_android_28.perludilindungi.faskes.*

open class FaskesFragment : Fragment(), DaftarFaskesInterface {

    companion object {
        fun newInstance() = FaskesFragment()
    }

    private lateinit var viewModel: DaftarFaskesViewModel
    private var fragmentIneractionListener : OnFragmentInteractionListener? = null
    val db by lazy { this.activity?.let { FaskesDB(it) } }
    private lateinit var binding: FaskesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FaskesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(DaftarFaskesViewModel::class.java)

        if (activity is MainActivity) {
            fetchFaskes("DKI JAKARTA", "KOTA ADM. JAKARTA PUSAT")
        } else {
            fetchBookmark()
        }

    }

    override fun onResume() {
        super.onResume()

        if (activity is BookmarkActivity) {
            db?.let { viewModel.makeDBCall(it)
        }}
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            fragmentIneractionListener = context
        }
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction()
    }


    fun fetchFaskes(provinsi: String, kota: String) {
        viewModel.getFaskesDataObserver().observe(viewLifecycleOwner, Observer<List<Faskes>>{
            if (it != null) {
                binding.faskesRv.layoutManager = LinearLayoutManager(activity)
                binding.faskesRv.adapter = FaskesAdapter(it, this)

            } else {
                Toast.makeText(this.context, "Error", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.makeFaskesApiCall(provinsi, kota, 0.0, 0.0)
    }

    fun fetchBookmark() {
        viewModel.getFaskesDataObserver().observe(viewLifecycleOwner, Observer<List<Faskes>>{
            if (it != null) {
                binding.faskesRv.layoutManager = LinearLayoutManager(activity)
                binding.faskesRv.adapter = FaskesAdapter(it, this)

            } else {
                Toast.makeText(this.context, "Error", Toast.LENGTH_LONG).show()
            }
        })
        db?.let { viewModel.makeDBCall(it) }
    }

    override fun onItemClick(position: Int) {
        println(viewModel.faskesArray[position])
        val faskes = viewModel.faskesArray[position]

        val intent = Intent(this.activity, DetailActivity::class.java).apply {
            putExtra("FASKES", faskes)
        }
        startActivity(intent)

    }


}