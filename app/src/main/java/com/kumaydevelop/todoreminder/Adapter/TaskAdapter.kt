package com.kumaydevelop.todoreminder.Adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kumaydevelop.todoreminder.Model.Task
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

class TaskAdapter(data: OrderedRealmCollection<Task>?) : RealmBaseAdapter<Task>(data) {

    inner class ViewHolder(cell: View) {
        val date = cell.findViewById<TextView>(android.R.id.text1)
        val title = cell.findViewById<TextView>(android.R.id.text2)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val viewHolder : ViewHolder

        when (convertView) {
            null -> {
                val inflater = LayoutInflater.from(parent?.context)
                view = inflater.inflate(android.R.layout.simple_expandable_list_item_2, parent, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            }
            else -> {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }
        }

        adapterData?.run {
            val schedule = get(position)
            // 年月日と時間を表示する
            viewHolder.date.setText(DateFormat.format("yyyy/MM/dd", schedule.date).toString() + "  " + DateFormat.format("HH:mm", schedule.time).toString())
            viewHolder.title.text = schedule.title
        }
        return view
    }
}