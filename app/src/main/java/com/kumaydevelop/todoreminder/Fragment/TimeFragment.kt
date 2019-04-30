package com.kumaydevelop.todoreminder.Fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.TimePicker
import java.util.*

class TimeFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    interface onTimeSelectListner {
        fun onSelected(hourOfDay: Int, minutes: Int)
    }

    private lateinit var listner: onTimeSelectListner

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is onTimeSelectListner) {
            listner = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minutes = c.get(Calendar.MINUTE)
        return TimePickerDialog(context, this, hour, minutes, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listner.onSelected(hourOfDay, minute)
    }
}