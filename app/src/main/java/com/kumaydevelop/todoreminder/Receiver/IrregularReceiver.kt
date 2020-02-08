package com.kumaydevelop.todoreminder.Receiver

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kumaydevelop.todoreminder.model.Task
import com.kumaydevelop.todoreminder.Util.DateUtil
import io.realm.Realm
import io.realm.kotlin.where
import java.text.SimpleDateFormat
import java.util.*

class IrregularReceiver : BroadcastReceiver() {

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
            val date = DateUtil.toDate("yyyy/MM/dd HH:mm","${df.format(task.date)} ${dd.format(task.time)}")
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
}