package com.kumaydevelop.todoreminder.Receiver

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kumaydevelop.todoreminder.Model.Task
import io.realm.Realm
import io.realm.kotlin.where
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RebootReceiver : BroadcastReceiver() {

    private lateinit var realm : Realm

    override fun onReceive(context: Context?, intent: Intent?) {

        realm = Realm.getDefaultInstance()
        val tasks = realm.where<Task>().findAll()

        // アラームの再設定を行う
        for(task in tasks) {
            val alarm = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmBroadCastReceiver::class.java)
            intent.putExtra("task_id", task.id)
            intent.putExtra("title", task.title)

            val df = SimpleDateFormat("yyyy/MM/dd")
            val dd = SimpleDateFormat("HH:mm")
            // タスク期限をyyyy/MM/dd HH:mm形式で取得する
            val date = "${df.format(task.date)} ${dd.format(task.time)}".toDate()
            val calendar = Calendar.getInstance()
            calendar.time = date
            val pending = PendingIntent.getBroadcast(context, task.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val info = AlarmManager.AlarmClockInfo(calendar.timeInMillis, null)
            alarm.setAlarmClock(info, pending)

            val now = Date()

            // タスク期限が今より前なら、通知を送る
            if (date!!.before(now)) {
                val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val alarmBroadCastReceiver = AlarmBroadCastReceiver()
                alarmBroadCastReceiver.notification(context, manager, intent)
            }
        }
    }

    // 日付形式に変更する
    fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date? {
        val sdFormat = try {
            SimpleDateFormat(pattern)
        } catch (e: IllegalArgumentException) {
            null
        }
        val date = sdFormat?.let {
            try {
                it.parse(this)
            } catch (e: ParseException) {
                null
            }
        }
        return date
    }


}