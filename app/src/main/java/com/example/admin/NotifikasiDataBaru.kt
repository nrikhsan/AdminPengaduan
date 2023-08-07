package com.example.admin

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.admin.utils.EnumClass
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channel_id_data_baru = "cahnnel_id"
const val channel_data_baru_name = "com.example.admin"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class NotifikasiDataBaru : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null){
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }

    fun getRemoteView(nik : String, catatan : String) : RemoteViews {
        val remoteViews = RemoteViews("com.example.admin", R.layout.layout_notifikasi)
        remoteViews.setTextViewText(R.id.tvNotif, EnumClass.getNik(nik))
        remoteViews.setTextViewText(R.id.tvDeskripsi, catatan)

        return remoteViews
    }


    fun generateNotification(id: String, nik : String, catatan : String){
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra(EditActivity.PENGADU_ID, id)
        intent.putExtra(EditActivity.NIK, nik)
        intent.putExtra(EditActivity.CATATAN, catatan)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        //channel id, channel name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channel_id_data_baru)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(nik, catatan))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(channel_id_data_baru, channel_data_baru_name, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(0,builder.build())
    }

}