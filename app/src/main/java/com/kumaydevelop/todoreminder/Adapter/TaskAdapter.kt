package com.kumaydevelop.todoreminder.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.kumaydevelop.todoreminder.Model.TaskDetail
import com.kumaydevelop.todoreminder.databinding.TaskItemBinding

class TaskAdapter(val context: Context, var listDatas: List<TaskDetail>) : BaseAdapter() {

    override fun getCount(): Int {
        return listDatas.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return listDatas[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var binding: TaskItemBinding?

        if (convertView == null) {
            binding = TaskItemBinding.inflate(LayoutInflater.from(context), parent,false)
        } else {
            // 今まで使っていたものを使いまわす
            binding = convertView.tag as TaskItemBinding
        }

        binding!!.taskDetail = listDatas[position]
        // root化してviewを返す
        return binding!!.root
    }
}