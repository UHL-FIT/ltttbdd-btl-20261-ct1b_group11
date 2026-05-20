package com.btl.buddybudget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.btl.buddybudget.data.repo.Repo
import com.btl.buddybudget.ui.danhmuc.DanhMucViewModel
import com.btl.buddybudget.ui.vi.SuaViViewModel
import com.btl.buddybudget.ui.vi.ThemViViewModel
import com.btl.buddybudget.ui.vi.ViViewModel


class AppViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ViViewModel::class.java) ->
                ViViewModel(repo) as T

            modelClass.isAssignableFrom(ThemViViewModel::class.java) ->
                ThemViViewModel(repo) as T
            modelClass.isAssignableFrom(SuaViViewModel::class.java) ->
                SuaViViewModel(repo) as T

            modelClass.isAssignableFrom(DanhMucViewModel::class.java) ->
                DanhMucViewModel(repo) as T

            else -> throw IllegalArgumentException(
                "ViewModel không được đăng ký trong Factory: ${modelClass.name}"
            )
        }
    }
}