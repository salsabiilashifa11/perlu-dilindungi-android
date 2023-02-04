package com.if3210_2022_android_28.perludilindungi.faskes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.if3210_2022_android_28.perludilindungi.R
import com.if3210_2022_android_28.perludilindungi.model.Faskes

class FaskesAdapter(private val faskesList : List<Faskes>, private val daftarFaskesInterface: DaftarFaskesInterface)
    : RecyclerView.Adapter<FaskesAdapter.FaskesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaskesViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.faskes_card, parent, false)
        return FaskesViewHolder(itemView, daftarFaskesInterface)
    }

    override fun onBindViewHolder(holder: FaskesViewHolder, position: Int) {

        val currentItem = faskesList[position]
        holder.itemView.findViewById<TextView>(R.id.nama_faskes_text).text = currentItem.nama

        if (currentItem.jenis_faskes == "RUMAH SAKIT") {
            holder.itemView.findViewById<TextView>(R.id.tag_faskes_text)
                .setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.rumahSakitColor))
        } else if (currentItem.jenis_faskes == "KLINIK") {
            holder.itemView.findViewById<TextView>(R.id.tag_faskes_text)
                .setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.klinikColor))
        }
        holder.itemView.findViewById<TextView>(R.id.tag_faskes_text).text = currentItem.jenis_faskes
        if (currentItem.jenis_faskes == "" || currentItem.jenis_faskes == null) {
            holder.itemView.findViewById<TextView>(R.id.tag_faskes_text).text = "LAINNYA"
        }

        holder.itemView.findViewById<TextView>(R.id.alamat_text).text = currentItem.alamat
        holder.itemView.findViewById<TextView>(R.id.notelp_text).text = currentItem.telp
        holder.itemView.findViewById<TextView>(R.id.kode_faskes_text).text = currentItem.kode

        holder.itemView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                daftarFaskesInterface.onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return faskesList.size
    }

    class FaskesViewHolder(itemView: View, daftarFaskesInterface: DaftarFaskesInterface) : RecyclerView.ViewHolder(itemView)
}