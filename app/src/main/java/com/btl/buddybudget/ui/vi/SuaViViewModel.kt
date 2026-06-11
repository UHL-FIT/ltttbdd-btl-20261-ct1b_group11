package com.btl.buddybudget.ui.vi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btl.buddybudget.data.db.entities.Vi
import com.btl.buddybudget.data.repo.Repo
import kotlinx.coroutines.launch

class SuaViViewModel(
    private val repo: Repo
) : ViewModel() {

    var uiState by mutableStateOf(SuaViState())
        private set

    fun loadWallet(walletId: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            // Gọi phương thức từ repo
            val vi = repo.layViTheoId(walletId)
            if (vi != null) {

                uiState = SuaViState(
                    id = vi.id,
                    name = vi.name,
                   // soDu = "0",
                    donVi = vi.donVi,
                    iconName = vi.iconName,
                    colorHex = vi.colorHex,
                    isArchived = vi.isArchived,
                    sortOrder = vi.sortOrder,
                    isLoading = false
                )
            } else {
                uiState = uiState.copy(error = "Không tìm thấy thông tin ví", isLoading = false)
            }
        }
    }

    fun doiTenVi(value: String)  { uiState = uiState.copy(name = value, error = null) }
    fun doiDonVi(value: String)  { uiState = uiState.copy(donVi = value) }
    fun doiMau(value: String)    { uiState = uiState.copy(colorHex = value) }
    fun doiIcon(value: String)   { uiState = uiState.copy(iconName = value) }
    fun anVi(value: Boolean)     { uiState = uiState.copy(isArchived = value) }
    fun clearError()             { uiState = uiState.copy(error = null) }

    fun capNhatVi(onSuccess: () -> Unit = {}) {
        if (uiState.name.isBlank()) {
            uiState = uiState.copy(error = "Tên ví không được để trống")
            return
        }


        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val viCapNhat = Vi(
                id = uiState.id,
                name = uiState.name.trim(),
                donVi = uiState.donVi.ifBlank { "VND" },
                iconName = uiState.iconName,
                colorHex = uiState.colorHex,
                isArchived = uiState.isArchived,
                sortOrder = uiState.sortOrder
            )
            repo.suaVi(viCapNhat)
            uiState = uiState.copy(isLoading = false)
            onSuccess()
        }
    }

    // Hàm Xóa Ví khỏi hệ thống
    fun xoaVi(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true)

                // Kiểm tra xem có giao dịch nào đang sử dụng ví này không (Theo kiểu danh mục)
                val coTheXoa = repo.coTheXoaVi(uiState.id)
                if (!coTheXoa) {
                    uiState = uiState.copy(
                        error = "Không thể xóa ví đã có lịch sử chi tiêu",
                        isLoading = false
                    )
                    return@launch
                }

                val viXoa = Vi(
                    id = uiState.id,
                    name = uiState.name,
                    donVi = uiState.donVi,
                    iconName = uiState.iconName,
                    colorHex = uiState.colorHex,
                    isArchived = uiState.isArchived,
                    sortOrder = uiState.sortOrder
                )
                
                repo.xoaVi(viXoa)
                uiState = uiState.copy(isLoading = false)
                onSuccess()
            } catch (e: Exception) {
                uiState = uiState.copy(error = "Lỗi khi xoá: ${e.message}", isLoading = false)
            }
        }
    }


}