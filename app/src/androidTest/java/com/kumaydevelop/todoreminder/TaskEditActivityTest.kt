package com.kumaydevelop.todoreminder

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.RootMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kumaydevelop.todoreminder.Activity.TaskEditActivity
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskEditActivityTest {

    @Rule
    @JvmField

    public val rule = ActivityTestRule(TaskEditActivity::class.java)

    /**
     * 新規登録の場合、削除ボタンが表示されていないことを確認
     */
    @Test
    fun isButtonInvisible() {
        onView(ViewMatchers.withId(R.id.delete)).check(matches(Matchers.not(ViewMatchers.isCompletelyDisplayed())))
    }

    /**
     * タイトル必須入力チェック
     */
    @Test
    fun register_invalid_title() {
        onView(withId(R.id.titleEdit)).perform(replaceText (""))
        onView(withId(R.id.detailEdit)).perform(replaceText ("テスト"))
        onView(withId(R.id.dateText)).perform(replaceText ("2019/04/20"))
        onView(withId(R.id.dateText)).perform(replaceText ("20:30"))
        onView(withId(R.id.save)).perform(click())
        onView(withText("タスク名とタスク期限は入力必須です")).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    /**
     * 期限必須入力チェック
     */
    @Test
    fun register_invalid_dateLimit() {
        onView(withId(R.id.titleEdit)).perform(replaceText ("テスト"))
        onView(withId(R.id.detailEdit)).perform(replaceText ("テスト"))
        onView(withId(R.id.dateText)).perform(replaceText (""))
        onView(withId(R.id.dateText)).perform(replaceText (""))
        onView(withId(R.id.save)).perform(click())
        onView(withText("タスク名とタスク期限は入力必須です")).inRoot(isDialog()).check(matches(isDisplayed()));
    }
}