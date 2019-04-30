package com.kumaydevelop.todoreminder.Fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.widget.DatePicker
import java.util.*

class DateFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    interface onDateSelectListnerIn {
        fun onSelected(year: Int, month: Int, date: Int)
    }

    private var listner: onDateSelectListnerIn? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is onDateSelectListnerIn) {
            listner = context
        }
    }

    // API23以下でもリスナーを取得できるようにする処理
//    override fun onAttach(activity: Activity?) {
//        super.onAttach(activity)
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return
//        if (activity is onDateSelectListnerIn) {
//            listner = activity
//        }
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val date = cal.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity, this, year, month, date)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        listner?.onSelected(year, month, dayOfMonth)
    }
}