package com.kumaydevelop.todoreminder.Adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.kumaydevelop.todoreminder.Model.TaskDetail
import com.kumaydevelop.todoreminder.R
import com.kumaydevelop.todoreminder.databinding.TaskItemBinding

class TaskAdapter(val context: Context) : BaseAdapter() {

    var tasks: List<TaskDetail> = emptyList()

    override fun getCount(): Int {
        return tasks.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return tasks[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var binding: TaskItemBinding?

        if (convertView == null) {
            // カスタム作成したtask_item.xmlを使う
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.task_item,parent,false)
            binding.root.tag = binding
        } else {
            // 今まで使っていたものを使いまわす
            binding = convertView.tag as TaskItemBinding
        }

        binding!!.taskDetail = getItem(position) as TaskDetail
        // root化してviewを返す
        return binding.root
    }
}