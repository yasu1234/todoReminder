package com.kumaydevelop.todoreminder.viewmodel

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.lifecycle.ViewModel

class EditViewModel: ViewModel() {

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
}