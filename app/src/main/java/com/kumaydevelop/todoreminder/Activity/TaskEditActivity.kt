package com.kumaydevelop.todoreminder.Activity

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.Spanned
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.kumaydevelop.todoreminder.Fragment.DateFragment
import com.kumaydevelop.todoreminder.Fragment.TimeFragment
import com.kumaydevelop.todoreminder.Model.Task
import com.kumaydevelop.todoreminder.NotificationTime
import com.kumaydevelop.todoreminder.R
import com.kumaydevelop.todoreminder.Receiver.AlarmBroadCastReceiver
import com.kumaydevelop.todoreminder.Util.CalenderUtil
import com.kumaydevelop.todoreminder.Util.DateUtil
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_task_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.*

class TaskEditActivity : AppCompatActivity(), DateFragment.onDateSelectListnerIn, TimeFragment.onTimeSelectListner {

    private lateinit var realm : Realm

    // spinnerの初期値
    var item : String = NotificationTime.UNSETTING.time

    var selectedCode : Int = 0

    // 日時選択時の処理
    override fun onSelected(year: Int, month: Int, date: Int) {
        val cal = Calendar.getInstance()
        cal.set(year, month, date)
        dateText.setText(android.text.format.DateFormat.format("yyyy/MM/dd", cal))
    }

    // 時間選択時の処理
    override fun onSelected(hourOfDay: Int, minutes: Int) {
        // 時間をHH:mm形式で表示させる
        timeText.setText("%1$02d:%2$02d".format(hourOfDay, minutes))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_edit)
        realm = Realm.getDefaultInstance()

        initSpinner()

        // (必須)のみ赤文字にする
        val timelimitHtml = "タスク期限<font color=red>(必須)</font><br>入力欄をタップして設定してください"
        timeLimit.setText(toSpanned(timelimitHtml))
        val titleHtml = "タスク名<font color=red>(必須)</font>"
        titleName.setText(toSpanned(titleHtml))

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
            val notifyCode = NotificationTime.values().filter { it.code == task?.notifyTime }.first()
            spinner.setSelection(notifyCode.code)
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
                        DateUtil.toDate("yyyy/MM/dd",dateText.text.toString())?.let {
                            task.date = it
                        }
                        DateUtil.toDate("HH:mm",timeText.text.toString())?.let {
                            task.time = it
                        }
                        val notifyCode = NotificationTime.values().filter { it.time == item }.first()
                        selectedCode = notifyCode.code
                        task.title = titleEdit.text.toString()
                        task.detail = detailEdit.text.toString()
                        task.notifyTime = selectedCode
                    }

                    alert("タスクを追加しました") {
                        yesButton {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }
                    }.show()
                    val date = DateUtil.toDate("yyyy/MM/dd HH:mm", "${dateText.text} ${timeText.text}")
                    val calendar = CalenderUtil.adjustNotifyTime(selectedCode, date!!)
                    // 設定した時間にアラームが作動するようにする
                    setAlarmManager(calendar, nextId, titleEdit.text.toString())
                }
                else -> {
                    realm.executeTransaction {
                        val task = realm.where<Task>().equalTo("id", taskId).findFirst()
                        // 年月日を登録する
                        DateUtil.toDate("yyyy/MM/dd",dateText.text.toString())?.let {
                            task?.date = it
                        }

                        // 時間を登録する
                        DateUtil.toDate("HH:mm",timeText.text.toString())?.let {
                            task?.time = it
                        }
                        val notifyCode = NotificationTime.values().filter { it.time == item }.first()
                        selectedCode = notifyCode.code
                        task?.title = titleEdit.text.toString()

                        task?.detail = detailEdit.text.toString()
                        task?.notifyTime = selectedCode
                    }

                    alert("タスクを更新しました") {
                        yesButton {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }
                    }.show()
                    // 選択した年月日と時間をまとめる
                    val date = DateUtil.toDate("yyyy/MM/dd HH:mm", "${dateText.text} ${timeText.text}")
                    val calendar = CalenderUtil.adjustNotifyTime(selectedCode, date!!)
                    // 設定した時間にアラームが作動するようにする
                    setAlarmManager(calendar, taskId!!, titleEdit.text.toString())
                }
            }
        }

        // 削除ボタンを押下したときの処理
        delete.setOnClickListener {
            realm.executeTransaction {
                realm.where<Task>().equalTo("id", taskId)?.findFirst()?.deleteFromRealm()
            }
            alert("タスクを削除しました") {
                yesButton {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
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

        // フォーカスが外れたときキーボードを非表示にする(詳細)
        detailEdit.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }

        // フォーカスが外れたときキーボードを非表示にする(タスク名)
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

    // HTMLのタグを適用する
    fun toSpanned(html: String) : Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(html)
        }
    }

    // spinner作成
    fun initSpinner() {
        var selectedOption = arrayListOf<String>()

        // 選択肢の作成
        NotificationTime.values().map {
            selectedOption.add(it.time)
        }

        val adapter = ArrayAdapter(applicationContext,
                android.R.layout.simple_spinner_item, selectedOption)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        // リスナーを登録
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            // アイテムが選択された時
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // spinnerを取得
                val spinnerParent = parent as Spinner
                item = spinnerParent.selectedItem as String

            }

            // アイテムが選択されなかった
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}
