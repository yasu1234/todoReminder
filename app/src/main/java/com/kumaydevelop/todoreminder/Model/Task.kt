package com.kumaydevelop.todoreminder.Model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Task: RealmObject() {
    @PrimaryKey
    var id : Long = 0
    var title: String = ""
    var detail: String = ""
    var date : Date = Date()
    var time : Date = Date()
    var deleteFlg : Int = 0
}