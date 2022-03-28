package me.fabricyo.quemdeve.application

import android.app.Application
import me.fabricyo.quemdeve.helpers.HelperDB

class DevedorApplication : Application() {

    var helperDB: HelperDB? = null
        private set

    companion object {
        lateinit var instance: DevedorApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDB = HelperDB(this)
    }
}