package com.example.posyandu.features.daftarKader

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.posyandu.BuildConfig
import com.example.posyandu.databinding.CardKaderBinding
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class DaftarKaderAdapter(val context: Context) :
    ListAdapter<KaderItem, DaftarKaderAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardKaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(context, item)

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, item)
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: KaderItem)
    }

    class MyViewHolder(val binding: CardKaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var prefs: SharedPreferences

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(context: Context, kader: KaderItem) {
            prefs = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
            val token = prefs.getString("token", "no token")

            val user = kader.user
            val tanggalLahir = user.tanggalLahir
            val birthDate = LocalDate.parse(tanggalLahir, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val today = LocalDate.now()
            val period = Period.between(birthDate, today)

            binding.namaRemaja.text = user.nama
            binding.umurRemaja.text = "${period.years} tahun"

            val glideUrl = GlideUrl(
                BuildConfig.BASE_URL + "file/" + user.foto,
                LazyHeaders.Builder().addHeader("Authorization", "Bearer $token").build()
            )
            Glide.with(binding.root).load(glideUrl).into(binding.foto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<KaderItem>() {
            override fun areItemsTheSame(
                oldItem: KaderItem,
                newItem: KaderItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: KaderItem,
                newItem: KaderItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}