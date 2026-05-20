package com.btl.buddybudget.ui.giaodich


// State đại diện cho toàn bộ dữ liệu trên màn hình Thêm giao dịch
data class ThemGiaoDichScreenState(
    val idExpense: Boolean = true,             // true: Khoản chi, false: Khoản thu
    val amount: String = "",                    // Số tiền nhập vào
    val selectedWalletName: String = "Ví tín dụng", // Tên ví đang chọn
    val selectedGroupName: String = "Chọn nhóm"   // Tên nhóm (danh mục) đang chọn
)