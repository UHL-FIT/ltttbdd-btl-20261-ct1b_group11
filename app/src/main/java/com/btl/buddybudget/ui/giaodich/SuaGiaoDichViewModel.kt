package com.btl.buddybudget.ui.giaodich

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btl.buddybudget.data.db.entities.GiaoDich
import com.btl.buddybudget.data.repo.Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SuaGiaoDichViewModel(private val repo: Repo) : ViewModel() {

    private val _uiState = MutableStateFlow(ThemGiaoDichScreenState())
    val uiState: StateFlow<ThemGiaoDichScreenState> = _uiState.asStateFlow()

    private var currentTransaction: GiaoDich? = null

    fun loadTransaction(id: Int) {
        viewModelScope.launch {
            repo.layGiaoDichTheoId(id)?.let { gd ->
                currentTransaction = gd
                val danhMuc = repo.layDanhMucTheoId(gd.idDanhMuc)
                val vi = repo.layViTheoId(gd.idVi)
                
                _uiState.update { 
                    it.copy(
                        idExpense = gd.type == "EXPENSE",
                        amount = gd.amount.toLong().toString(),
                        idVi = gd.idVi,
                        selectedWalletName = vi?.name ?: "Ví không xác định",
                        selectedWalletColor = vi?.colorHex ?: "#48484A",
                        selectedWalletIcon = vi?.iconName ?: "👛",
                        selectedGroupName = danhMuc?.name ?: "Chọn nhóm",
                        selectedGroupColor = danhMuc?.colorHex ?: "#48484A",
                        selectedGroupIcon = danhMuc?.iconName ?: "📁",
                        idDanhMuc = gd.idDanhMuc,
                        note = gd.note,
                        date = gd.date
                    )
                }
            }
        }
    }
/*
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

 */

    fun onAmountChanged(newAmount: String) {
        if (newAmount.all { it.isDigit() } || newAmount.isEmpty()) {
            _uiState.update { it.copy(amount = newAmount) }
        }
    }

    fun onWalletSelected(id: Int, name: String, color: String, icon: String) {
        _uiState.update { it.copy(idVi = id, selectedWalletName = name, selectedWalletColor = color, selectedWalletIcon = icon) }
    }

    fun onGroupSelected(id: Int, name: String, color: String, icon: String) {
        _uiState.update { 
            it.copy(
                idDanhMuc = id, 
                selectedGroupName = name,
                selectedGroupColor = color,
                selectedGroupIcon = icon
            ) 
        }
    }

    fun onNoteChanged(newNote: String) {
        _uiState.update { it.copy(note = newNote) }
    }

    fun onDateChanged(newDate: Long) {
        _uiState.update { it.copy(date = newDate) }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun updateTransaction() {
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

        val transaction = currentTransaction?.copy(
            amount = amountValue,
            idDanhMuc = currentState.idDanhMuc,
            idVi = currentState.idVi,
            type = if (currentState.idExpense) "EXPENSE" else "INCOME",
            date = currentState.date,
            note = currentState.note
        ) ?: return

        viewModelScope.launch {
            repo.suaGiaoDich(transaction)
            _uiState.update { it.copy(successMessage = "Cập nhật giao dịch thành công") }
        }
    }

    fun deleteTransaction() {
        val transaction = currentTransaction ?: return
        viewModelScope.launch {
            repo.xoaGiaoDich(transaction)
            _uiState.update { it.copy(successMessage = "Xóa giao dịch thành công") }
        }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
}
