package com.kumaydevelop.todoreminder.Application

import android.app.Application
import io.realm.Realm

class TaskSchedulerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}