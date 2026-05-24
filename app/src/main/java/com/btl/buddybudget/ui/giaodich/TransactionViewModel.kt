package com.btl.buddybudget.ui.giaodich

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btl.buddybudget.data.db.KieuGiaoDich
import com.btl.buddybudget.data.db.quanhe.GiaoDichvaDanhMuc
import com.btl.buddybudget.data.repo.Repo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class TransactionViewModel(private val repo: Repo) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    private val currentCalendar = Calendar.getInstance()
    private var selectedMonth = currentCalendar.get(Calendar.MONTH) + 1
    private var selectedYear = currentCalendar.get(Calendar.YEAR)
    private var selectedWalletId: Int? = null

    private val _searchResults = MutableStateFlow<List<GiaoDichvaDanhMuc>>(emptyList())
    val searchResults: StateFlow<List<GiaoDichvaDanhMuc>> = _searchResults.asStateFlow()

    init {
        loadWallets()
        loadTransactions()
        loadTotalBalance()
    }

    private fun loadWallets() {
        viewModelScope.launch {
            repo.layViDangDung().collect { wallets ->
                _uiState.update { it.copy(wallets = wallets) }
            }
        }
    }

    private fun loadTotalBalance() {
        viewModelScope.launch {
            if (selectedWalletId == null) {
                repo.layTongTaiSan().collect { balance ->
                    _uiState.update { it.copy(totalBalance = balance) }
                }
            } else {
                repo.layViVaSoDu().collect { walletsWithBalance ->
                    val wallet = walletsWithBalance.find { it.id == selectedWalletId }
                    _uiState.update { it.copy(totalBalance = wallet?.soDuHienTai ?: 0.0) }
                }
            }
        }
    }

    fun onWalletSelected(walletId: Int?, walletName: String) {
        selectedWalletId = walletId
        _uiState.update { 
            it.copy(
                selectedWalletId = walletId,
                walletName = walletName,
                isWalletPickerVisible = false 
            ) 
        }
        loadTransactions()
        loadTotalBalance()
    }

    fun toggleWalletPicker(visible: Boolean) {
        _uiState.update { it.copy(isWalletPickerVisible = visible) }
    }

    fun onDateSelected(month: Int, year: Int) {
        selectedMonth = month
        selectedYear = year
        _uiState.update { 
            it.copy(
                selectedMonth = month, 
                selectedYear = year,
                isDatePickerVisible = false 
            ) 
        }
        loadTransactions()
    }

    fun toggleDatePicker(visible: Boolean) {
        _uiState.update { it.copy(isDatePickerVisible = visible) }
    }

    fun deleteTransaction(transaction: GiaoDichvaDanhMuc) {
        viewModelScope.launch {
            repo.xoaGiaoDich(transaction.giaodich)
            loadTransactions()
            loadTotalBalance()
        }
    }

    fun searchTransactions(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            repo.timKiemGiaoDich(query).collect { results ->
                _searchResults.value = results
            }
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val transactionsFlow = if (selectedWalletId == null) {
                repo.layGiaoDichTheoThang(selectedMonth, selectedYear)
            } else {
                repo.layGiaoDichTheoViVaThang(selectedWalletId!!, selectedMonth, selectedYear)
            }
            
            transactionsFlow.collect { transactions ->
                val income = transactions.filter { it.giaodich.type == KieuGiaoDich.INCOME.name }.sumOf { it.giaodich.amount }
                val expense = transactions.filter { it.giaodich.type == KieuGiaoDich.EXPENSE.name }.sumOf { it.giaodich.amount }
                
                // Nhóm theo Danh mục (Category)
                val grouped = transactions.groupBy { it.danhmuc?.name ?: "Khác" }

                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        incomeAmount = income,
                        expenseAmount = expense,
                        groupedTransactions = grouped
                    )
                }
            }
        }
    }
}
