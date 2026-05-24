package com.btl.buddybudget.ui.danhmuc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btl.buddybudget.data.db.KieuGiaoDich
import com.btl.buddybudget.data.db.entities.DanhMuc
import com.btl.buddybudget.data.repo.Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThemDanhMucViewModel(private val repo: Repo) : ViewModel() {

    private val _uiState = MutableStateFlow(ThemDanhMucState())
    val uiState: StateFlow<ThemDanhMucState> = _uiState.asStateFlow()

    fun capNhatTen(ten: String) {
        _uiState.update { it.copy(tenDanhMuc = ten) }
    }

    fun capNhatLoai(loai: KieuGiaoDich) {
        _uiState.update { it.copy(loaiGiaoDich = loai) }
    }

    fun capNhatIcon(icon: String) {
        _uiState.update { it.copy(iconChon = icon) }
    }

    fun capNhatMau(mau: String) {
        _uiState.update { it.copy(mauChon = mau) }
    }

    fun luuDanhMuc() {
        val ten = _uiState.value.tenDanhMuc
        if (ten.isBlank()) {
            _uiState.update { it.copy(thongBaoLoi = "Vui lòng nhập tên nhóm") }
            return
        }

        viewModelScope.launch {
            try {
                val danhMucMoi = DanhMuc(
                    name = ten,
                    iconName = _uiState.value.iconChon,
                    colorHex = _uiState.value.mauChon,
                    type = _uiState.value.loaiGiaoDich,
                    isDefault = false
                )
                repo.themDanhMuc(danhMucMoi)
                _uiState.update { it.copy(daLuuThanhCong = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(thongBaoLoi = "Lỗi khi lưu: ${e.message}") }
            }
        }
    }
    
    fun resetTrangThaiLuu() {
        _uiState.update { it.copy(daLuuThanhCong = false) }
    }
}
