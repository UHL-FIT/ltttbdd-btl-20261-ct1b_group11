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

class SuaDanhMucViewModel(private val repo: Repo) : ViewModel() {

    private val _uiState = MutableStateFlow(SuaDanhMucState())
    val uiState: StateFlow<SuaDanhMucState> = _uiState.asStateFlow()

    fun loadDanhMuc(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val danhMuc = repo.layDanhMucTheoId(id)
            if (danhMuc != null) {
                _uiState.update {
                    it.copy(
                        id = danhMuc.id,
                        tenDanhMuc = danhMuc.name,
                        loaiGiaoDich = danhMuc.type,
                        iconChon = danhMuc.iconName,
                        mauChon = danhMuc.colorHex,
                        isDefault = danhMuc.isDefault,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(thongBaoLoi = "Không tìm thấy danh mục", isLoading = false) }
            }
        }
    }

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

    fun luuCapNhat() {
        val state = _uiState.value
        if (state.tenDanhMuc.isBlank()) {
            _uiState.update { it.copy(thongBaoLoi = "Vui lòng nhập tên nhóm") }
            return
        }

        viewModelScope.launch {
            try {
                val danhMucCapNhat = DanhMuc(
                    id = state.id,
                    name = state.tenDanhMuc,
                    iconName = state.iconChon,
                    colorHex = state.mauChon,
                    type = state.loaiGiaoDich,
                    isDefault = state.isDefault
                )
                repo.suaDanhMuc(danhMucCapNhat)
                _uiState.update { it.copy(daCapNhat = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(thongBaoLoi = "Lỗi khi cập nhật: ${e.message}") }
            }
        }
    }

    fun xoaDanhMuc() {
        val state = _uiState.value
        if (state.isDefault) {
            _uiState.update { it.copy(thongBaoLoi = "Không thể xoá danh mục mặc định") }
            return
        }

        viewModelScope.launch {
            try {
                val danhMucXoa = DanhMuc(
                    id = state.id,
                    name = state.tenDanhMuc,
                    iconName = state.iconChon,
                    colorHex = state.mauChon,
                    type = state.loaiGiaoDich,
                    isDefault = state.isDefault
                )
                repo.xoaDanhMuc(danhMucXoa)
                _uiState.update { it.copy(daXoa = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(thongBaoLoi = "Lỗi khi xoá: ${e.message}") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(thongBaoLoi = null) }
    }
}
