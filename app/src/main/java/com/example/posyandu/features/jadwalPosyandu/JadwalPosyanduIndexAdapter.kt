package com.example.posyandu.features.jadwalPosyandu

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.posyandu.databinding.CardJadwalPosyanduBinding
import java.text.SimpleDateFormat
import java.util.Locale

class JadwalPosyanduIndexAdapter :
    ListAdapter<JadwalPosyandu, JadwalPosyanduIndexAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CardJadwalPosyanduBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val jadwal = getItem(position)
        holder.bind(jadwal)

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, jadwal)
            }
        }
    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: JadwalPosyandu)
    }

    class MyViewHolder(val binding: CardJadwalPosyanduBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(jadwal: JadwalPosyandu) {
            val waktuMulai = jadwal.waktuMulai
            val waktuSelesai = jadwal.waktuSelesai

            val tanggal = waktuMulai.substring(0, 10)

            val format = SimpleDateFormat("yyyy-MM-dd", Locale("id"))
            val date = format.parse(tanggal)

            val hariFormat = SimpleDateFormat("EEEE", Locale("id"))
            val hari = hariFormat.format(date!!)

            val jamMulai = waktuMulai.substring(11, 16)
            val jamSelesai = waktuSelesai.substring(11, 16)

            binding.hari.text = hari
            binding.tanggal.text = tanggal
            binding.mulai.text = "$jamMulai WIB"
            binding.selesai.text = "$jamSelesai WIB"
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JadwalPosyandu>() {
            override fun areItemsTheSame(
                oldItem: JadwalPosyandu,
                newItem: JadwalPosyandu
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: JadwalPosyandu,
                newItem: JadwalPosyandu
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}