package com.kumaydevelop.todoreminder

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.kumaydevelop.todoreminder.Activity.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var rule = ActivityTestRule(MainActivity::class.java)

    /**
     * 画面に登録ボタンが表示されているかのチェック
     */
    @Test
    fun isButtonInvisible() {
        onView(withId(R.id.fab)).check(matches(isCompletelyDisplayed()))
    }
}