package com.kumaydevelop.todoreminder.Application

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class TaskSchedulerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val builder = RealmConfiguration.Builder()
        builder.schemaVersion(1L).migration(Migration())
        val config = builder.build()
        Realm.setDefaultConfiguration(config)
    }
}