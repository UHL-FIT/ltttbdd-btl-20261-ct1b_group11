package com.btl.buddybudget.ui.giaodich


// State đại diện cho toàn bộ dữ liệu trên màn hình Thêm giao dịch
data class ThemGiaoDichScreenState(
    val idExpense: Boolean = true,
    val amount: String = "",
    val selectedWalletName: String = "Chọn ví",
    val idVi: Int = 0,
    val selectedGroupName: String = "Chọn nhóm",
    val selectedWalletIcon: String = "💳",
    val selectedWalletColor: String = "#48484A",
    val selectedGroupIcon: String = "📁",
    val selectedGroupColor: String = "#48484A", // Màu mặc định
    val idDanhMuc: Int = 0,
    val note: String = "",
    val date: Long = System.currentTimeMillis(),
    val errorMessage: String? = null
)