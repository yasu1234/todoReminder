package com.kumaydevelop.todoreminder

enum class NotificationTime(val code: Int, val time: String) {
    UNSETTING(0,"0分(指定なし)"),
    FIVEMINUTES(1,"5分前"),
    TENMINUTES(2,"10分前"),
    FIFTEENMINUTES(3,"15分前"),
    THIRTYMINUTES(4,"30分前"),
    HOUR(5,"1時間前")
}

