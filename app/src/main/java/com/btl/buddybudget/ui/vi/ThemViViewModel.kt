package com.btl.buddybudget.ui.vi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btl.buddybudget.data.db.entities.Vi
import com.btl.buddybudget.data.repo.Repo
import kotlinx.coroutines.launch

class ThemViViewModel(
    private val repo: Repo
) : ViewModel() {

    var uiState by mutableStateOf(ViUIState())
        private set

    fun doiTenVi(value: String)  { uiState = uiState.copy(name = value, error = null) }
    fun doiSoDu(value: String)   { uiState = uiState.copy(soDu = value, error = null) }
    fun doiDonVi(value: String)  { uiState = uiState.copy(donVi = value) }
    fun doiMau(value: String)    { uiState = uiState.copy(colorHex = value) }
    fun doiIcon(value: String)   { uiState = uiState.copy(iconName = value) }
    fun anVi(value: Boolean)     { uiState = uiState.copy(isArchived = value) }


    fun taoVi(onSuccess: () -> Unit = {}) {
        if (uiState.name.isBlank()) {
            uiState = uiState.copy(error = "Tên ví không được để trống")
            return
        }

        val amount = uiState.soDu.toDoubleOrNull() ?: run {
            if (uiState.soDu.isNotBlank()) {
                uiState = uiState.copy(error = "Số dư không hợp lệ")
                return
            }
            0.0
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            repo.themVi(
                Vi(
                    name      = uiState.name.trim(),
                    soDuBanDau = amount,
                    donVi     = uiState.donVi.ifBlank { "VND" },
                    iconName  = uiState.iconName,
                    colorHex  = uiState.colorHex,
                    isArchived = uiState.isArchived
                )
            )
            uiState = uiState.copy(isLoading = false)
            onSuccess()
        }
    }
}