package com.kumaydevelop.todoreminder.Receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.kumaydevelop.todoreminder.Activity.MainActivity
import com.kumaydevelop.todoreminder.R
import io.realm.Realm

/**
 * 通知を受け取り表示するクラス
 */
class AlarmBroadCastReceiver : BroadcastReceiver() {

    private lateinit var realm: Realm

    override fun onReceive(context: Context?, intent: Intent?) {
        realm = Realm.getDefaultInstance()

        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notification(context, manager, intent!!)
    }

    // 通知を行う
    fun notification(context: Context?, manager: NotificationManager, intent: Intent) {
        createChannel(manager)
        createNotification(context, intent, manager)
    }

    // ANdroid8以上でチャンネルを作る
    fun createChannel(manager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("new scheduler", "タスク期限", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "タスクスケジューラーのチャンネルです"
            channel.enableVibration(true)
            channel.setShowBadge(true)
            manager.createNotificationChannel(channel)
        }
    }

    // 通知を作成する
    fun createNotification(context: Context?, intent: Intent?, manager: NotificationManager) {
        val title = intent?.getStringExtra("title")
        val builder = NotificationCompat.Builder(context!!, "new scheduler")

        // 通知をタップしたときに一覧画面に遷移するようにする
        val pending = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0)

        val notification = builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("設定した時間になりました。タスクが完了したら削除してください")
                .setContentText(title)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pending)
                .build()

        val taskId = intent?.getLongExtra("task_id", -1L)

        // taskIdを設定し、別々の通知が来るようにする
        manager.notify(taskId!!.toInt(), notification)
    }
}