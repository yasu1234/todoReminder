package com.kumaydevelop.todoreminder.ViewModel

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.lifecycle.ViewModel
import com.kumaydevelop.todoreminder.model.Task
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class EditViewModel: ViewModel() {

    private lateinit var realm : Realm

    fun getPresentTask(taskId: Long, realm: Realm): Task? {
        val task = realm.where<Task>().equalTo("id", taskId).findFirst()
        return task
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