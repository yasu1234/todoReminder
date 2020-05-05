package com.kumaydevelop.todoreminder.ViewModel

import androidx.lifecycle.ViewModel
import android.text.format.DateFormat
import com.kumaydevelop.todoreminder.model.Task
import com.kumaydevelop.todoreminder.model.TaskDetail
import io.realm.Realm
import io.realm.kotlin.where

class ListViewModel: ViewModel() {

    private lateinit var realm: Realm

    fun loadData(realm: Realm): List<TaskDetail> {
        val tasks = realm.where<Task>().findAll().sort("id")
        val taskList = mutableListOf<TaskDetail>()

        // タスク一覧に表示するタイトルと期限をリスト化し、listviewに適用させる
        tasks.forEach { taskList.add(TaskDetail(it.title,
                DateFormat.format("yyyy/MM/dd", it.date).toString() + "  " + DateFormat.format("HH:mm", it.time).toString(), it.id)) }

        return taskList
    }

}
