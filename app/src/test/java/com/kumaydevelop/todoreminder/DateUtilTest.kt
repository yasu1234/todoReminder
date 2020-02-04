package com.kumaydevelop.todoreminder

import com.kumaydevelop.todoreminder.Util.DateUtil
import org.junit.Assert
import org.junit.Test

class DateUtilTest {

    /**
     * 正しい時間を取得(yyyy/MM/dd HH:mm)
     */
    @Test
    fun DateUtilTest_Normal001() {
        val dateUtil = DateUtil
        val date = dateUtil.toDate( "yyyy/MM/dd HH:mm","2019/12/20 11:11")
        Assert.assertEquals(date.toString(), "Fri Dec 20 11:11:00 JST 2019")
    }

    /**
     * 分違い
     */
    @Test
    fun DateUtilTest_Normal002() {
        val dateUtil = DateUtil
        val date = dateUtil.toDate( "yyyy/MM/dd HH:mm","2019/12/20 11:12")
        Assert.assertNotEquals(date.toString(), "Fri Dec 20 11:11:00 JST 2019")
    }

    /**
     * 正しい時間を取得(yyyy/MM/dd)
     */
    @Test
    fun DateUtilTest_Normal003() {
        val dateUtil = DateUtil
        val date = dateUtil.toDate( "yyyy/MM/dd","2019/12/20")
        Assert.assertEquals(date.toString(), "Fri Dec 20 00:00:00 JST 2019")
    }

    /**
     * 日違い
     */
    @Test
    fun DateUtilTest_Normal004() {
        val dateUtil = DateUtil
        val date = dateUtil.toDate( "yyyy/MM/dd","2019/12/20")
        Assert.assertNotEquals(date.toString(), "Fri Dec 19 00:00:00 JST 2019")
    }

    /**
     * 年違い
     */
    @Test
    fun DateUtilTest_Normal005() {
        val dateUtil = DateUtil
        val date = dateUtil.toDate( "yyyy/MM/dd","2019/12/20")
        Assert.assertNotEquals(date.toString(), "Fri Dec 19 00:00:00 JST 2020")
    }

    /**
     * 月違い
     */
    @Test
    fun DateUtilTest_Normal006() {
        val dateUtil = DateUtil
        val date = dateUtil.toDate( "yyyy/MM/dd","2019/11/20")
        Assert.assertNotEquals(date.toString(), "Fri Dec 19 00:00:00 JST 2020")
    }

    /**
     * 形式違い(年月日のみ指定している)
     */
    @Test
    fun DateUtilTest_AbNormal001() {
        val dateUtil = DateUtil
        val date = dateUtil.toDate( "yyyy/MM/dd HH:mm","2019/12/20")
        Assert.assertNull(date)
    }

    /**
     * 形式違い(時間のみ指定している)
     */
    @Test
    fun DateUtilTest_AbNormal006() {
        val dateUtil = DateUtil
        val date = dateUtil.toDate( "yyyy/MM/dd HH:mm","11:12")
        Assert.assertNull(date)
    }
}