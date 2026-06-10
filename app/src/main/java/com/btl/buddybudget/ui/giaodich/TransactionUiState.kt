package com.btl.buddybudget.ui.giaodich

import com.btl.buddybudget.data.db.TransactionViewMode
import com.btl.buddybudget.data.db.entities.Vi
import com.btl.buddybudget.data.db.quanhe.GiaoDichvaDanhMucvaVi
import java.util.Calendar

data class TransactionUiState(
    val isLoading: Boolean = false,

    val totalBalance: Double = 0.0,
    val incomeAmount: Double = 0.0,
    val expenseAmount: Double = 0.0,

    val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),

    val groupedTransactions: Map<String, List<GiaoDichvaDanhMucvaVi>> = emptyMap(),
    val viewMode: TransactionViewMode = TransactionViewMode.BY_CATEGORY,

    val walletName: String = "Tổng cộng",
    val selectedWalletId: Int? = null,
    val wallets: List<Vi> = emptyList(),
    val isWalletPickerVisible: Boolean = false,
    val error: String? = null,
    val isDatePickerVisible: Boolean = false
)
