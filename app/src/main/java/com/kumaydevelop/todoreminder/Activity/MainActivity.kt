package com.kumaydevelop.todoreminder.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.kumaydevelop.todoreminder.R
import com.kumaydevelop.todoreminder.Model.Task
import com.kumaydevelop.todoreminder.Adapter.TaskAdapter
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        realm = Realm.getDefaultInstance()
        val tasks = realm.where<Task>().findAll()
        listView.adapter = TaskAdapter(tasks)

        fab.setOnClickListener { view ->
            maxRegistCheck()
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            val task = parent.getItemAtPosition(position) as Task
            startActivity<TaskEditActivity>(
                    "task_id" to task.id
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                maxRegistCheck()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun maxRegistCheck() {
        realm = Realm.getDefaultInstance()
        val tasks = realm.where<Task>().findAll()
        if (tasks.count() == 5) {
            alert("5件までしか登録できません") {
                yesButton {  }
            }.show()
        } else {
            startActivity<TaskEditActivity>()
        }
    }
}
