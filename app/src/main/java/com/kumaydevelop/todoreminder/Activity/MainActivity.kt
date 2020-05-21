package com.kumaydevelop.todoreminder.Activity

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kumaydevelop.todoreminder.Adapter.TaskAdapter
import com.kumaydevelop.todoreminder.model.Task
import com.kumaydevelop.todoreminder.R
import com.kumaydevelop.todoreminder.viewmodel.ListViewModel
import com.kumaydevelop.todoreminder.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton

class MainActivity : AppCompatActivity(), LifecycleObserver {

    private lateinit var realm: Realm

    lateinit var listAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        realm = Realm.getDefaultInstance()

        // MainActivityをDataBinding化する
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val listViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        binding.listViewModel = listViewModel

        // ToDoデータを受け取り表示させる
        listViewModel._listItems.observe(this, Observer {
            listAdapter = TaskAdapter(this, it)
            binding.listview.adapter = listAdapter
        })

        // ⊕ボタンを押下したときの処理
        fab.setOnClickListener { view ->
            maxRegistCheck()
        }

        // 一覧のデータを押下したときの処理
        binding.setOnItemClick { parent, view, position, id ->
            val task = realm.where<Task>().equalTo("id", listAdapter.listDatas[position].id).findFirst()
            startActivity<TaskEditActivity>(
                    "task_id" to task!!.id
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 登録を押下したときの処理
        return when (item.itemId) {
            R.id.action_settings -> {
                maxRegistCheck()
                true
            }
            // タスク登録・編集画面に遷移
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    // 5件までしか登録できないようにバリデーションチェックを行う
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
