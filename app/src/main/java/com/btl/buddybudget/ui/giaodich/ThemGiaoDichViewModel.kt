package com.btl.buddybudget.ui.giaodich

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.btl.buddybudget.data.db.entities.GiaoDich
import com.btl.buddybudget.data.repo.Repo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ThemGiaoDichViewModel(private val repo: Repo) : ViewModel() {

    // Quản lý trạng thái UI dưới dạng StateFlow
    private val _uiState = MutableStateFlow(ThemGiaoDichScreenState())
    val uiState: StateFlow<ThemGiaoDichScreenState> = _uiState.asStateFlow()

    // Thay đổi loại giao dịch (Thu / Chi)
    fun onTransactionTypeChanged(isExpense: Boolean) {
        _uiState.update { 
            it.copy(
                idExpense = isExpense,
                idDanhMuc = 0,
                selectedGroupName = "Chọn nhóm",
                selectedGroupColor = "#48484A"
            ) 
        }
    }

    fun resetForm() {
        _uiState.value = ThemGiaoDichScreenState()

    }

    // Thay đổi số tiền nhập vào
    fun onAmountChanged(newAmount: String) {
        // Chỉ cho phép nhập số
        if (newAmount.all { it.isDigit() } || newAmount.isEmpty()) {
            _uiState.update { it.copy(amount = newAmount, errorMessage = null) }
        }
    }

    // Cập nhật ví được chọn (Khi từ trang Chọn ví trả kết quả về)
    fun onWalletSelected(id: Int, name: String, color: String, icon: String) {
        _uiState.update { it.copy(idVi = id, selectedWalletName = name, selectedWalletColor = color, selectedWalletIcon = icon) }
    }

    // Cập nhật nhóm được chọn (Khi từ trang Chọn nhóm trả kết quả về)
    fun onGroupSelected(id: Int, name: String, color: String, icon: String) {
        _uiState.update { 
            it.copy(
                idDanhMuc = id, 
                selectedGroupName = name,
                selectedGroupColor = color,
                selectedGroupIcon = icon,
                errorMessage = null
            ) 
        }
    }

    // Cập nhật ghi chú
    fun onNoteChanged(newNote: String) {
        _uiState.update { it.copy(note = newNote) }
    }

    // Cập nhật ngày giao dịch
    fun onDateChanged(newDate: Long) {
        _uiState.update { it.copy(date = newDate) }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    // Thực hiện lưu giao dịch vào database
    fun saveTransaction() {
        val currentState = _uiState.value
        val amountValue = currentState.amount.toDoubleOrNull() ?: 0.0

        if (currentState.idVi <= 0) {
            _uiState.update { it.copy(errorMessage = "Vui lòng chọn nhóm ví") }
            return
        }

        if (amountValue <= 0) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập số tiền hợp lệ") }
            return
        }

        if (currentState.idDanhMuc <= 0) {
            _uiState.update { it.copy(errorMessage = "Vui lòng chọn nhóm giao dịch") }
            return
        }


        if (currentState.note.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập ghi chú") }
            return
        }

        val transaction = GiaoDich(
            amount = amountValue,
            idDanhMuc = currentState.idDanhMuc,
            idVi = currentState.idVi,
            type = if (currentState.idExpense) "EXPENSE" else "INCOME",
            date = currentState.date,
            note = currentState.note
        )

        viewModelScope.launch {
            repo.themGiaoDich(transaction)
            _uiState.update { it.copy(successMessage = "Thêm giao dịch thành công") }
        }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
}
