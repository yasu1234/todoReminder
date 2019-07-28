package com.kumaydevelop.todoreminder.ViewModel

import android.arch.lifecycle.ViewModel
import android.text.format.DateFormat
import com.kumaydevelop.todoreminder.Model.Task
import com.kumaydevelop.todoreminder.Model.TaskDetail
import io.realm.Realm
import io.realm.kotlin.where

class ListViewModel: ViewModel() {

    private lateinit var realm: Realm

    fun loadData(): List<TaskDetail> {
        realm = Realm.getDefaultInstance()
        val tasks = realm.where<Task>().findAll().sort("id")
        val taskList = mutableListOf<TaskDetail>()

        // タスク一覧に表示するタイトルと期限をリスト化し、listviewに適用させる
        tasks.forEach { taskList.add(TaskDetail(it.title,
                DateFormat.format("yyyy/MM/dd", it.date).toString() + "  " + DateFormat.format("HH:mm", it.time).toString(), it.id)) }

        return taskList
    }

}
