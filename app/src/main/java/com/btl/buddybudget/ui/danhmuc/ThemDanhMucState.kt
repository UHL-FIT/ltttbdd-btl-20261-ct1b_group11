package com.btl.buddybudget.ui.danhmuc

import com.btl.buddybudget.data.db.KieuGiaoDich

data class ThemDanhMucState(
    val tenDanhMuc: String = "",
    val loaiGiaoDich: KieuGiaoDich = KieuGiaoDich.EXPENSE,
    val iconChon: String = "📁",
    val mauChon: String = "#4CAF50",
    val daLuuThanhCong: Boolean = false,
    val thongBaoLoi: String? = null
)

// Danh sách mẫu để người dùng chọn
val DanhSachIcons = listOf("📁", "🍴", "🛒", "🚗", "🏠", "💡", "🎬", "🏥", "🎓", "🎁", "💰", "🍕", "☕", "🎮", "👕")
val DanhSachMau = listOf("#4CAF50", "#2196F3", "#F44336", "#FFEB3B", "#9C27B0", "#FF9800", "#795548", "#607D8B", "#E91E63", "#00BCD4")
