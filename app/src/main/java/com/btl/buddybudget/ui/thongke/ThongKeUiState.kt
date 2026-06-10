package com.btl.buddybudget.ui.thongke

import com.btl.buddybudget.data.db.quanhe.ThongKeDanhMuc
import java.util.Calendar

data class ThongKeUiState(
    val isLoading: Boolean = false,
    val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val isExpenseSelected: Boolean = true,
    val items: List<ThongKeDanhMuc> = emptyList(),
    val isDatePickerVisible: Boolean = false
)
