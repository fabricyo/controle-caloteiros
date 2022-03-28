package me.fabricyo.quemdeve.bases

import android.database.sqlite.SQLiteDatabase
import me.fabricyo.quemdeve.helpers.HelperDB

open class BaseRepository(
    val helperDB: HelperDB? = null
) {
    var readableDatabase: SQLiteDatabase? = null
        private set
        get() {
            return helperDB?.readableDatabase
        }

    var writeableDatabase: SQLiteDatabase? = null
        private set
        get() {
            return helperDB?.writableDatabase
        }


}