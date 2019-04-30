package com.kumaydevelop.todoreminder.Activity

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.kumaydevelop.todoreminder.*
import com.kumaydevelop.todoreminder.Fragment.DateFragment
import com.kumaydevelop.todoreminder.Fragment.TimeFragment
import com.kumaydevelop.todoreminder.Model.Task
import com.kumaydevelop.todoreminder.Receiver.AlarmBroadCastReceiver
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_task_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TaskEditActivity : AppCompatActivity(), DateFragment.onDateSelectListnerIn, TimeFragment.onTimeSelectListner {

    private lateinit var realm : Realm

    override fun onSelected(year: Int, month: Int, date: Int) {
        val cal = Calendar.getInstance()
        cal.set(year, month, date)
        dateText.setText(android.text.format.DateFormat.format("yyyy/MM/dd", cal))
    }

    override fun onSelected(hourOfDay: Int, minutes: Int) {
        // 時間をHH:mm形式で表示させる
        timeText.setText("%1$02d:%2$02d".format(hourOfDay, minutes))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_edit)
        realm = Realm.getDefaultInstance()

        // カーソルを表示させず、年月日時の選択だけできるようにする
        dateText.isEnabled = true
        dateText.isCursorVisible = false
        dateText.isFocusable = false
        timeText.isEnabled = true
        timeText.isCursorVisible = false
        timeText.isFocusable = false

        var nextId : Long = 0L

        val taskId = intent?.getLongExtra("task_id", -1L)
        // タスク更新の場合
        if (taskId != -1L) {
            val task = realm.where<Task>().equalTo("id", taskId).findFirst()
            dateText.setText(android.text.format.DateFormat.format("yyyy/MM/dd", task?.date))
            timeText.setText(android.text.format.DateFormat.format("HH:mm", task?.time))
            titleEdit.setText(task?.title)
            detailEdit.setText(task?.detail)
            delete.visibility = View.VISIBLE
        } else {
            delete.visibility = View.INVISIBLE
        }

        save.setOnClickListener {
            // 必須項目入力チェック
            if (dateText.text.toString() == "" || titleEdit.text.toString() == "" || timeText.text.toString() == "") {
                alert("タスク期限とタスク名は入力必須です") {
                    yesButton {  }
                }.show()
                return@setOnClickListener
            }
            when(taskId) {
            // 新規登録の場合
                -1L -> {
                    realm.executeTransaction {
                        val maxId = realm.where<Task>().max("id")
                        // 登録のときのIdは+1した状態にする
                        nextId = (maxId?.toLong() ?: 0L) + 1
                        val task = realm.createObject<Task>(nextId)
                        dateText.text.toString().toDate("yyyy/MM/dd")?.let {
                            task.date = it
                        }
                        timeText.text.toString().toDate("HH:mm")?.let {
                            task.time = it
                        }
                        task.title = titleEdit.text.toString()
                        task.detail = detailEdit.text.toString()
                    }

                    alert("タスクを追加しました") {
                        yesButton { finish() }
                    }.show()
                    val date = "${dateText.text} ${timeText.text}".toDate()
                    val calender = Calendar.getInstance()
                    calender.time = date
                    // 設定した時間にアラームが作動するようにする
                    setAlarmManager(calender, nextId, titleEdit.text.toString())
                }
                else -> {
                    realm.executeTransaction {
                        val task = realm.where<Task>().equalTo("id", taskId).findFirst()
                        // 年月日を登録する
                        dateText.text.toString().toDate("yyyy/MM/dd")?.let {
                            task?.date = it
                        }

                        // 時間を登録する
                        timeText.text.toString().toDate("HH:mm")?.let {
                            task?.time = it
                        }
                        task?.title = titleEdit.text.toString()
                        task?.detail = detailEdit.text.toString()
                    }

                    alert("タスクを更新しました") {
                        yesButton { finish() }
                    }.show()
                    // 選択した年月日と時間をまとめる
                    val date = "${dateText.text} ${timeText.text}".toDate()
                    val calender = Calendar.getInstance()
                    calender.time = date
                    // 設定した時間にアラームが作動するようにする
                    setAlarmManager(calender, taskId!!, titleEdit.text.toString())
                }
            }
        }

        delete.setOnClickListener {
            realm.executeTransaction {
                realm.where<Task>().equalTo("id", taskId)?.findFirst()?.deleteFromRealm()
            }
            alert("タスクを削除しました") {
                yesButton { finish() }
            }.show()
            deleteAlarmManager(taskId!!)
        }

        // 年月日選択ダイアログを表示させる
        dateText.setOnClickListener {
            val dialog = DateFragment()
            dialog.show(supportFragmentManager, "date_dialog")
        }

        // 時間選択ダイアログを表示させる
        timeText.setOnClickListener {
            val dialog = TimeFragment()
            dialog.show(supportFragmentManager,"time_dialog")
        }

        // フォーカスが外れたときキーボードを非表示にする
        detailEdit.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }

        // フォーカスが外れたときキーボードを非表示にする
        titleEdit.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        focusView.requestFocus()
        return super.onTouchEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

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


    // アラームをセットする
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setAlarmManager(calendar: Calendar, taskId: Long, title: String) {
        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadCastReceiver::class.java)
        intent.putExtra("task_id", taskId)
        intent.putExtra("title", title)
        val pending = PendingIntent.getBroadcast(this, taskId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val info = AlarmManager.AlarmClockInfo(calendar.timeInMillis, null)
        alarm.setAlarmClock(info, pending)

    }

    // アラームをキャンセルする
    private fun deleteAlarmManager(taskId: Long) {
        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadCastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(this, taskId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarm.cancel(pending)
    }
}
