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
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

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
            val user = remaja.user
            val tanggalLahir = user.tanggalLahir
            val birthDate = LocalDate.parse(tanggalLahir, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val today = LocalDate.now()
            val period = Period.between(birthDate, today)
            val isKader = remaja.isKader
            val pemeriksaan = remaja.pemeriksaan

            binding.namaRemaja.text = user.nama
            binding.umurRemaja.text = "${period.years} tahun"

            if (pemeriksaan.id == 0) {
                binding.beratChip.visibility = View.GONE
                binding.tinggiChip.visibility = View.GONE
                binding.statusDrawNull.visibility = View.VISIBLE
                binding.statusTextNull.visibility = View.VISIBLE
                binding.statusDrawOk.visibility = View.GONE
                binding.statusTextOk.visibility = View.GONE
                binding.statusDrawRisk.visibility = View.GONE
                binding.statusTextRisk.visibility = View.GONE
            } else {
                binding.beratChip.text = "${pemeriksaan.beratBadan} kg"
                binding.tinggiChip.text = "${pemeriksaan.tinggiBadan} cm"

                binding.statusDrawNull.visibility = View.GONE
                binding.statusTextNull.visibility = View.GONE

                if (pemeriksaan.berisiko()) {
                    binding.statusDrawOk.visibility = View.GONE
                    binding.statusTextOk.visibility = View.GONE
                    binding.statusDrawRisk.visibility = View.VISIBLE
                    binding.statusTextRisk.visibility = View.VISIBLE
                } else {
                    binding.statusDrawRisk.visibility = View.GONE
                    binding.statusTextRisk.visibility = View.GONE
                    binding.statusDrawOk.visibility = View.VISIBLE
                    binding.statusTextOk.visibility = View.VISIBLE
                }
            }

            if (!isKader) {
                binding.kaderChip.visibility = View.GONE
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