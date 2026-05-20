package com.btl.buddybudget

import android.app.Application

class BuddyBudgetApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}