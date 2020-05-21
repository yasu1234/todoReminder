package com.kumaydevelop.todoreminder.viewmodel

import android.text.format.DateFormat
import androidx.lifecycle.*
import com.kumaydevelop.todoreminder.model.Task
import com.kumaydevelop.todoreminder.model.TaskDetail
import io.realm.Realm
import io.realm.kotlin.where

class ListViewModel: ViewModel(), LifecycleObserver {

    var _listItems :MutableLiveData<List<TaskDetail>> = loadData()

    private lateinit var realm: Realm

    fun loadData(): MutableLiveData<List<TaskDetail>> {
        realm = Realm.getDefaultInstance()
        _listItems = MutableLiveData<List<TaskDetail>>()
        val tasks = realm.where<Task>().findAll().sort("id")
        val taskList = mutableListOf<TaskDetail>()

        // タスク一覧に表示するタイトルと期限をリスト化し、listviewに適用させる
        tasks.forEach { taskList.add(TaskDetail(it.title,
                DateFormat.format("yyyy/MM/dd", it.date).toString() + "  " + DateFormat.format("HH:mm", it.time).toString(), it.id)) }

        _listItems.value = taskList

        return _listItems

    }
}
