package com.btl.buddybudget.ui.thongke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btl.buddybudget.data.db.KieuGiaoDich
import com.btl.buddybudget.data.repo.Repo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ThongKeViewModel(private val repo: Repo) : ViewModel() {

    private val _uiState = MutableStateFlow(ThongKeUiState())
    val uiState: StateFlow<ThongKeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun onTabSelected(isExpense: Boolean) {
        _uiState.update { it.copy(isExpenseSelected = isExpense) }
        loadData()
    }

    fun onDateSelected(month: Int, year: Int) {
        _uiState.update { 
            it.copy(
                selectedMonth = month, 
                selectedYear = year,
                isDatePickerVisible = false 
            ) 
        }
        loadData()
    }

    fun toggleDatePicker(visible: Boolean) {
        _uiState.update { it.copy(isDatePickerVisible = visible) }
    }

    private fun loadData() {
        val state = _uiState.value
        val type = if (state.isExpenseSelected) KieuGiaoDich.EXPENSE.name else KieuGiaoDich.INCOME.name
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repo.thongKeDanhMuc(state.selectedMonth, state.selectedYear, type)
                .collect { items ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            items = items
                        )
                    }
                }
        }
    }
}
