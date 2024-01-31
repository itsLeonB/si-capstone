package com.example.posyandu.features.pemeriksaan

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.posyandu.R
import com.example.posyandu.databinding.CardPemeriksaanBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PemeriksaanAdapter :
    ListAdapter<Pemeriksaan, PemeriksaanAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CardPemeriksaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pemeriksaan = getItem(position)
        holder.bind(pemeriksaan)

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, pemeriksaan)
            }
        }
    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: Pemeriksaan)
    }

    class MyViewHolder(val binding: CardPemeriksaanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(pemeriksaan: Pemeriksaan) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
            val date = inputFormat.parse(pemeriksaan.waktuPengukuran)
            val formattedDate = outputDateFormat.format(date)
            if (pemeriksaan.berisiko()) {
                binding.status.text = "Berisiko stunting"
                binding.status.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.md_theme_light_error
                    )
                )
                binding.statusDraw.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.error_cross
                    )
                )
            }
            binding.tanggal.text = formattedDate
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Pemeriksaan>() {
            override fun areItemsTheSame(
                oldItem: Pemeriksaan,
                newItem: Pemeriksaan
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Pemeriksaan,
                newItem: Pemeriksaan
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}