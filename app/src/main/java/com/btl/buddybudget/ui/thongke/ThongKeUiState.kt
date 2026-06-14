package com.btl.buddybudget.ui.thongke

import com.btl.buddybudget.data.db.quanhe.ThongKeDanhMuc
import java.util.Calendar

data class ThongKeUiState(
    val isLoading: Boolean = false,
    val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val isExpenseSelected: Boolean = true,
    val items: List<ThongKeDanhMuc> = emptyList(),
    val displayItems: List<ThongKeDisplayItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val isDatePickerVisible: Boolean = false
)

data class ThongKeDisplayItem(
    val name: String,
    val iconName: String,
    val colorHex: String,
    val total: Double,
    val percentage: Float, // 0.0 to 1.0
    val percentageText: String, // "50%"
    val sweepAngle: Float, // 0 to 360
    val formattedTotal: String // "1.000.000 đ"
)
