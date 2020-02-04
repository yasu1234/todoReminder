package com.kumaydevelop.todoreminder

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
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