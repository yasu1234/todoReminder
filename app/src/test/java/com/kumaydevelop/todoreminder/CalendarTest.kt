package com.kumaydevelop.todoreminder

import com.kumaydevelop.todoreminder.Util.CalenderUtil
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class CalendarTest {

    /**
     * タスク期限と通知時間が変わらない場合
     */
    @Test
    fun DateUtilTest_Normal001() {
        val calendarUtil = CalenderUtil
        val strDate = "2019/05/08 01:23"
        val date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(strDate)
        val calendar = calendarUtil.adjustNotifyTime(0,date)
        Assert.assertEquals(calendar.get(Calendar.YEAR), 2019)
        Assert.assertEquals(calendar.get(Calendar.MONTH), 4)
        Assert.assertEquals(calendar.get(Calendar.DATE), 8)
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 1)
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 23)
    }

    /**
     * 通知時間がタスク期限の5分前
     */
    @Test
    fun DateUtilTest_Normal002() {
        val calendarUtil = CalenderUtil
        val strDate = "2019/05/08 01:23"
        val date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(strDate)
        val calendar = calendarUtil.adjustNotifyTime(1,date)
        Assert.assertEquals(calendar.get(Calendar.YEAR), 2019)
        Assert.assertEquals(calendar.get(Calendar.MONTH), 4)
        Assert.assertEquals(calendar.get(Calendar.DATE), 8)
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 1)
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 18)
    }

    /**
     * 通知時間がタスク期限の10分前
     */
    @Test
    fun DateUtilTest_Normal003() {
        val calendarUtil = CalenderUtil
        val strDate = "2019/05/08 01:23"
        val date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(strDate)
        val calendar = calendarUtil.adjustNotifyTime(2,date)
        Assert.assertEquals(calendar.get(Calendar.YEAR), 2019)
        Assert.assertEquals(calendar.get(Calendar.MONTH), 4)
        Assert.assertEquals(calendar.get(Calendar.DATE), 8)
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 1)
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 13)
    }

    /**
     * 通知時間がタスク期限の15分前
     */
    @Test
    fun DateUtilTest_Normal004() {
        val calendarUtil = CalenderUtil
        val strDate = "2019/05/08 01:23"
        val date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(strDate)
        val calendar = calendarUtil.adjustNotifyTime(3,date)
        Assert.assertEquals(calendar.get(Calendar.YEAR), 2019)
        Assert.assertEquals(calendar.get(Calendar.MONTH), 4)
        Assert.assertEquals(calendar.get(Calendar.DATE), 8)
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 1)
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 8)
    }

    /**
     * 通知時間がタスク期限の30分前
     */
    @Test
    fun DateUtilTest_Normal005() {
        val calendarUtil = CalenderUtil
        val strDate = "2019/05/08 01:33"
        val date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(strDate)
        val calendar = calendarUtil.adjustNotifyTime(4,date)
        Assert.assertEquals(calendar.get(Calendar.YEAR), 2019)
        Assert.assertEquals(calendar.get(Calendar.MONTH), 4)
        Assert.assertEquals(calendar.get(Calendar.DATE), 8)
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 1)
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 3)
    }

    /**
     * 通知時間がタスク期限の1時間前
     */
    @Test
    fun DateUtilTest_Normal006() {
        val calendarUtil = CalenderUtil
        val strDate = "2019/05/08 01:23"
        val date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(strDate)
        val calendar = calendarUtil.adjustNotifyTime(5,date)
        Assert.assertEquals(calendar.get(Calendar.YEAR), 2019)
        Assert.assertEquals(calendar.get(Calendar.MONTH), 4)
        Assert.assertEquals(calendar.get(Calendar.DATE), 8)
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0)
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 23)
    }

    /**
     * 通知時間とタスク期限の日にちが違う場合
     */
    @Test
    fun DateUtilTest_Normal007() {
        val calendarUtil = CalenderUtil
        val strDate = "2019/05/08 00:23"
        val date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(strDate)
        val calendar = calendarUtil.adjustNotifyTime(5,date)
        Assert.assertEquals(calendar.get(Calendar.YEAR), 2019)
        Assert.assertEquals(calendar.get(Calendar.MONTH), 4)
        Assert.assertEquals(calendar.get(Calendar.DATE), 7)
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 23)
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 23)
    }

    /**
     * 通知時間とタスク期限の時(hour)が違う場合
     */
    @Test
    fun DateUtilTest_Normal008() {
        val calendarUtil = CalenderUtil
        val strDate = "2019/05/08 01:23"
        val date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(strDate)
        val calendar = calendarUtil.adjustNotifyTime(4,date)
        Assert.assertEquals(calendar.get(Calendar.YEAR), 2019)
        Assert.assertEquals(calendar.get(Calendar.MONTH), 4)
        Assert.assertEquals(calendar.get(Calendar.DATE), 8)
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0)
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 53)
    }

    /**
     * 通知時間とタスク期限の月が違う場合
     */
    @Test
    fun DateUtilTest_Normal009() {
        val calendarUtil = CalenderUtil
        val strDate = "2019/05/01 00:23"
        val date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(strDate)
        val calendar = calendarUtil.adjustNotifyTime(5,date)
        Assert.assertEquals(calendar.get(Calendar.YEAR), 2019)
        Assert.assertEquals(calendar.get(Calendar.MONTH), 3)
        Assert.assertEquals(calendar.get(Calendar.DATE), 30)
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 23)
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 23)
    }

    /**
     * 想定外のコードが設定された場合、タスク期限が通知時間になる
     */
    @Test
    fun DateUtilTest_AbNormal001() {
        val calendarUtil = CalenderUtil
        val strDate = "2019/05/08 01:23"
        val date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(strDate)
        val calendar = calendarUtil.adjustNotifyTime(6,date)
        Assert.assertEquals(calendar.get(Calendar.YEAR), 2019)
        Assert.assertEquals(calendar.get(Calendar.MONTH), 4)
        Assert.assertEquals(calendar.get(Calendar.DATE), 8)
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 1)
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 23)
    }

}