package com.btl.buddybudget

import android.content.Context
import com.btl.buddybudget.data.repo.Repo
import com.btl.buddybudget.data.store.AppDatabase
import com.btl.buddybudget.data.store.UserSettings

class AppContainer(context: Context) {
    // Chỉ tạo Database 1 lần
    private val db = AppDatabase.layDataBase(context)

    // Khởi tạo UserSettings
    val userSettings by lazy { UserSettings(context) }

    // Repository được tạo ra ở đây
    val repo by lazy { Repo(
        db,
        db.daoGiaoDich(),
        db.daoDanhMuc(),
        db.daoVi()
    ) }

    /** Dùng trong Composable: viewModel(factory = container.viewModelFactory) */
    val viewModelFactory by lazy { AppViewModelFactory(repo) }
}