package com.example.admin

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin.databinding.ActivityMainBinding
import com.example.admin.utils.EnumClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataBase: DatabaseReference
    private lateinit var responsePengaduan: ArrayList<ResponsePengaduan>
    private val notifikasiDataBaru = NotifikasiDataBaru()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBase = FirebaseDatabase.getInstance().reference.child("pengaduan")
        responsePengaduan = arrayListOf()

        dataBase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                responsePengaduan.clear()
                if (snapshot.exists()) {
                    for (snapPengaduan in snapshot.children) {
                        val pengaduan = snapPengaduan.getValue(ResponsePengaduan::class.java)
                        responsePengaduan.add(pengaduan!!)

                        tampilNotifikasiDataBaru(pengaduan)
                    }
                    val adapterRiwayatPengaduan = AdapterRiwayatPengaduan(responsePengaduan)
                    binding.rvPengaduan.apply {
                        adapter = adapterRiwayatPengaduan
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        visibility = View.VISIBLE
                    }
                }else{
                    binding.rvPengaduan.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("ObsoleteSdkInt", "UnspecifiedImmutableFlag")
    private fun tampilNotifikasiDataBaru(pengaduan: ResponsePengaduan) {

        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra(EditActivity.PENGADU_ID, pengaduan.pengaduId)
        intent.putExtra(EditActivity.NIK, pengaduan.nik)
        intent.putExtra(EditActivity.CATATAN, pengaduan.catatan)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        notifikasiDataBaru.getRemoteView(EnumClass.getNik(pengaduan.nik.toString()), pengaduan.catatan.toString())

//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        //channel id, channel name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channel_id_data_baru)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
//            .setContentIntent(pendingIntent)

        builder = builder.setContent(notifikasiDataBaru.getRemoteView(pengaduan.nik.toString(), pengaduan.catatan.toString()))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channel_id_data_baru, channel_data_baru_name, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())
    }
}