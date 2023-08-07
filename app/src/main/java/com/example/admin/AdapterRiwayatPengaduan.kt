package com.example.admin

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.databinding.CardRiwayatPengaduanBinding
import com.example.admin.utils.EnumClass

class AdapterRiwayatPengaduan(private val pengaduan : ArrayList<ResponsePengaduan>) : RecyclerView.Adapter<AdapterRiwayatPengaduan.ViewHolder>() {

    inner class ViewHolder(private val binding : CardRiwayatPengaduanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ResponsePengaduan) {

            binding.apply {

                tvNik.text = EnumClass.getNik(result.nik.toString())
                tvCatatan.text = result.catatan
                tvTanggalRiwayat.text = result.tanggal

                val konfirmasi = result.konfirmasi
                if (konfirmasi){
                    tvKonfirmasi.text = "Telah dikonfirmasi"
                    tvKonfirmasi.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
                }else{
                    tvKonfirmasi.text = "Belum dikonfirmasi"
                    tvKonfirmasi.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark))
                }

            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, EditActivity::class.java)
                intent.putExtra(EditActivity.KONFIRMASI, result.konfirmasi)
                intent.putExtra(EditActivity.NIK, result.nik)
                intent.putExtra(EditActivity.CATATAN, result.catatan)
                intent.putExtra(EditActivity.TANGGAL, result.tanggal)
                intent.putExtra(EditActivity.PENGADU_ID, result.pengaduId)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardRiwayatPengaduanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = pengaduan.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = pengaduan[position]
        holder.bind(result)
    }
}