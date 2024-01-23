package com.example.posyandu.features.daftarRemaja

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.posyandu.databinding.CardRemajaBinding

class DaftarRemajaIndexAdapter :
    ListAdapter<RemajaDataItem, DaftarRemajaIndexAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CardRemajaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val remajaItem = getItem(position)
        holder.bind(remajaItem)

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, remajaItem)
            }
        }
    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: RemajaDataItem)
    }

    class MyViewHolder(val binding: CardRemajaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(remaja: RemajaDataItem) {
            val nama = remaja.user.nama
            val tanggalLahir = remaja.user.tanggalLahir
            val isKader = remaja.isKader

            binding.namaRemaja.text = nama
            binding.umurRemaja.text = tanggalLahir

            if (!isKader) {
                binding.kaderChip.visibility = View.INVISIBLE
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RemajaDataItem>() {
            override fun areItemsTheSame(
                oldItem: RemajaDataItem,
                newItem: RemajaDataItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RemajaDataItem,
                newItem: RemajaDataItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}