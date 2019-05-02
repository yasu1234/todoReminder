package com.kumaydevelop.todoreminder.Util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    /**
     * 文字列をDate型に変換
     * @param pattern 文字列の形式
     * @param input 変換する文字列
     *
     * @return date 日付のDate型
     *
     */
    fun toDate(pattern: String, input: String): Date? {
        val sdFormat = try {
            SimpleDateFormat(pattern)
        } catch (e: IllegalArgumentException) {
            null
        }
        val date = sdFormat?.let {
            try {
                it.parse(input)
            } catch (e: ParseException) {
                null
            }
        }
        return date
    }
}