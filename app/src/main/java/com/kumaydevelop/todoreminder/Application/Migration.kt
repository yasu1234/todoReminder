package com.kumaydevelop.todoreminder.Application

import io.realm.DynamicRealm
import io.realm.RealmMigration

class Migration : RealmMigration {

    override  fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val realmSchema = realm.schema
        // 変数に定義しなおす
        var oldVersion = oldVersion

        if (oldVersion == 0L) {
            val userSchema = realmSchema.get("Task")
            userSchema!!.renameField("deleteFlg", "notifyTime")
            oldVersion++
        }
    }
}