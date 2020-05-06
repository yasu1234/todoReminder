package com.kumaydevelop.todoreminder.ViewModel

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.lifecycle.ViewModel
import com.kumaydevelop.todoreminder.model.Task
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import com.kumaydevelop.todoreminder.NotificationTime

class EditViewModel: ViewModel() {

    // (必須)のみ赤文字にする
    val timelimitHtml = "タスク期限<font color=red>(必須)</font><br>入力欄をタップして設定してください"
    val taskLimitLabel = toSpanned(timelimitHtml)
    val titleHtml = "タスク名<font color=red>(必須)</font>"
    val titleLabel = toSpanned(titleHtml)

    var date: CharSequence = ""
    var time: CharSequence = ""
    var title: String =  ""
    var detail: String =  ""
    var isDeleteVisible = true

    // カーソルを表示させず、年月日時の選択だけできるようにする
    val isSpinnerEnable = true
    val isSpinnerCursorVisible = false
    val isSpinnerFocusable = false

    fun getPresentTask(taskId: Long, realm: Realm): Task? {
        val task = realm.where<Task>().equalTo("id", taskId).findFirst()
        return task
    }

    fun setPresentTask(task: Task) {
        date = android.text.format.DateFormat.format("yyyy/MM/dd", task.date)
        time = android.text.format.DateFormat.format("HH:mm", task.time)
        title = task.title
        detail = task.detail
        isDeleteVisible = false
    }

    fun createTask(realm: Realm): Task? {
        val maxId = realm.where<Task>().max("id")
        // 登録のときのIdは+1した状態にする
        val nextId = (maxId?.toLong() ?: 0L) + 1
        val task = realm.createObject<Task>(nextId)
        return task
    }

    fun deleteTask(taskId: Long, realm: Realm) {
        realm.where<Task>().equalTo("id", taskId)?.findFirst()?.deleteFromRealm()
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


}