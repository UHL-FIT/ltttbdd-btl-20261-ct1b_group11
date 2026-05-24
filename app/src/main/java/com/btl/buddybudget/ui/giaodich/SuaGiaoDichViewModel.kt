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
                        selectedWalletName = vi?.name ?: "Ví không xác định",
                        idVi = gd.idVi,
                        selectedGroupName = danhMuc?.name ?: "Chọn nhóm",
                        selectedGroupColor = danhMuc?.colorHex ?: "#48484A",
                        idDanhMuc = gd.idDanhMuc,
                        note = gd.note,
                        date = gd.date
                    )
                }
            }
        }
    }

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

    fun onAmountChanged(newAmount: String) {
        if (newAmount.all { it.isDigit() } || newAmount.isEmpty()) {
            _uiState.update { it.copy(amount = newAmount) }
        }
    }

    fun onWalletSelected(id: Int, name: String) {
        _uiState.update { it.copy(idVi = id, selectedWalletName = name) }
    }

    fun onGroupSelected(id: Int, name: String, color: String) {
        _uiState.update { 
            it.copy(
                idDanhMuc = id, 
                selectedGroupName = name,
                selectedGroupColor = color
            ) 
        }
    }

    fun onNoteChanged(newNote: String) {
        _uiState.update { it.copy(note = newNote) }
    }

    fun onDateChanged(newDate: Long) {
        _uiState.update { it.copy(date = newDate) }
    }

    fun updateTransaction(onSuccess: () -> Unit) {
        // ...
    }

    fun deleteTransaction(onSuccess: () -> Unit) {
        val transaction = currentTransaction ?: return
        viewModelScope.launch {
            repo.xoaGiaoDich(transaction)
            onSuccess()
        }
    }
}
