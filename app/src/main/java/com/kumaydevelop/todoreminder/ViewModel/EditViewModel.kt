package com.kumaydevelop.todoreminder.viewmodel

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.lifecycle.ViewModel
import com.kumaydevelop.todoreminder.model.Task
import io.realm.Realm
import io.realm.kotlin.where
import com.kumaydevelop.todoreminder.NotificationTime

class EditViewModel: ViewModel() {

    private lateinit var realm : Realm

    var taskTitle: String = ""
    var taskDetail: String = ""
    var taskDate: String = ""
    var taskTime: String = ""
    var isVisible = false

    // (必須)のみ赤文字にする
    val title = toSpanned("タスク期限<font color=red>(必須)</font><br>入力欄をタップして設定してください")
    val taskNameLabel = toSpanned("タスク名<font color=red>(必須)</font>")

    // HTMLのタグを適用する
    fun toSpanned(html: String) : Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(html)
        }
    }

    fun getPresentTask(taskId: Long): Task? {
        realm = Realm.getDefaultInstance()
        return realm.where<Task>().equalTo("id", taskId).findFirst()
    }

    fun setPresentTask(task: Task) {
        taskDate = android.text.format.DateFormat.format("yyyy/MM/dd", task.date).toString()
        taskTime = android.text.format.DateFormat.format("HH:mm", task.time).toString()
        taskTitle = task.title
        taskDetail = task.detail
        isVisible = true
    }
}