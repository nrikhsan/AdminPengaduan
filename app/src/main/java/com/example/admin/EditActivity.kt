package com.example.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.admin.databinding.ActivityEditBinding
import com.example.admin.utils.EnumClass
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var database: DatabaseReference
    companion object{
        const val PENGADU_ID = "pengaduId"
        const val NIK = "nik"
        const val CATATAN = "catatan"
        const val KONFIRMASI = "konfirmasi"
        const val TANGGAL = "tanggal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("pengaduan")

        val id = intent.getStringExtra(PENGADU_ID).toString()
        val nik = intent.getStringExtra(NIK).toString()
        val catatan = intent.getStringExtra(CATATAN).toString()
        val konfirmasi = intent.getBooleanExtra(KONFIRMASI, false)
        val tanggal = intent.getStringExtra(TANGGAL).toString()

        binding.toolbarAdmin.apply {
            title = EnumClass.getNik(nik)
            setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        binding.edtCatatan.setText(catatan)
        binding.edtTanggal.setText(tanggal)

        if (konfirmasi) {
            binding.btnKonfirmasi.visibility = View.GONE
            binding.btnBatalKonfirmasi.visibility = View.VISIBLE
        } else {
            binding.btnKonfirmasi.visibility = View.VISIBLE
            binding.btnBatalKonfirmasi.visibility = View.GONE
        }

        binding.btnUpdateData.setOnClickListener {
            updateData(id, nik,binding.edtCatatan.text.toString(), konfirmasi, binding.edtTanggal.text.toString())
        }

        binding.btnKonfirmasi.setOnClickListener {
            konfirmasiAduan(id, nik,binding.edtCatatan.text.toString(), konfirmasi, binding.edtTanggal.text.toString())
        }

        binding.btnBatalKonfirmasi.setOnClickListener {
            batalKonfirmasiAduan(id, nik,binding.edtCatatan.text.toString(), konfirmasi, binding.edtTanggal.text.toString())
        }

        binding.toolbarAdmin.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuHapus -> {
                    hapusPengaduan(id)
                    onBackPressedDispatcher.onBackPressed()
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun batalKonfirmasiAduan(id: String, nik: String, catatan: String, konfirmasi: Boolean, tanggal: String) {
        val batalKonfirmasi = FirebaseDatabase.getInstance().reference.child("pengaduan").child(id)
        val responsePengaduan = ResponsePengaduan(id, nik, catatan, false, tanggal)
        if (konfirmasi) {
            batalKonfirmasi.setValue(responsePengaduan)
            binding.btnKonfirmasi.visibility = View.VISIBLE
            binding.btnBatalKonfirmasi.visibility = View.GONE
            onBackPressedDispatcher.onBackPressed()
            Toast.makeText(
                this,
                "Berhasil membatalkan konfirmasi pengaduan",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            binding.btnKonfirmasi.visibility = View.VISIBLE
            binding.btnBatalKonfirmasi.visibility = View.GONE
        }
    }

    private fun konfirmasiAduan(id: String, nik: String, catatan: String, konfirmasi: Boolean, tanggal: String) {
        val konfirmasiDatabase = FirebaseDatabase.getInstance().reference.child("pengaduan").child(id)
        val responsePengaduan = ResponsePengaduan(id, nik, catatan, true, tanggal)
        if (konfirmasi) {
            binding.btnKonfirmasi.visibility = View.GONE
            binding.btnBatalKonfirmasi.visibility = View.VISIBLE
        } else {
            konfirmasiDatabase.setValue(responsePengaduan)
            binding.btnKonfirmasi.visibility = View.VISIBLE
            binding.btnBatalKonfirmasi.visibility = View.GONE
            onBackPressedDispatcher.onBackPressed()
            Toast.makeText(this, "Konfirmasi pengaduan berhasil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateData(id: String, nik: String, catatan: String, konfirmasi: Boolean, tanggal: String) {
        val updateData = FirebaseDatabase.getInstance().reference.child("pengaduan").child(id)
        val responsePengaduan = ResponsePengaduan(id, nik, catatan, konfirmasi, tanggal)
        updateData.setValue(responsePengaduan)
        Toast.makeText(this, "Berhasil mengupdate data pengaduan", Toast.LENGTH_SHORT).show()
    }

    private fun hapusPengaduan(id: String) {
        val deletData = FirebaseDatabase.getInstance().reference.child("pengaduan").child(id)
        val hapus = deletData.removeValue()
        hapus.addOnSuccessListener {
            Toast.makeText(this, "Berhasil menghapus pengaduan", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}