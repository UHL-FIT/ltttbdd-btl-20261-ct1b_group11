package com.btl.buddybudget.ui.thongke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btl.buddybudget.data.db.KieuGiaoDich
import com.btl.buddybudget.data.repo.Repo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

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
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repo.thongKeDanhMuc(state.selectedMonth, state.selectedYear, type)
                    .collect { items ->
                        val total = items.sumOf { it.total }
                        val currencyFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale.forLanguageTag("vi-VN")))
                        
                        val displayItems = items.map { item ->
                            val percentage = if (total > 0) (item.total / total).toFloat() else 0f
                            ThongKeDisplayItem(
                                name = item.name,
                                iconName = item.iconName,
                                colorHex = item.colorHex,
                                total = item.total,
                                percentage = percentage,
                                percentageText = "${(percentage * 100).toInt()}%",
                                sweepAngle = percentage * 360f,
                                formattedTotal = currencyFormat.format(item.total) + " đ"
                            )
                        }

                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                items = items,
                                displayItems = displayItems,
                                totalAmount = total
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Lỗi tải dữ liệu: ${e.message}") }
            }
        }
    }
}
