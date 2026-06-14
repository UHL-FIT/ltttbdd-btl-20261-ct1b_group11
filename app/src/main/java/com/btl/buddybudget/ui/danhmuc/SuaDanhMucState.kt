package com.btl.buddybudget.ui.danhmuc

import com.btl.buddybudget.data.db.KieuGiaoDich

data class SuaDanhMucState(
    val id: Int = 0,
    val tenDanhMuc: String = "",
    val loaiGiaoDich: KieuGiaoDich = KieuGiaoDich.EXPENSE,
    val iconChon: String = "📁",
    val mauChon: String = "#4CAF50",
    val originalLoai: KieuGiaoDich? = null,
    val originalIcon: String? = null,
    val isDefault: Boolean = false,
    val daCapNhat: Boolean = false,
    val daXoa: Boolean = false,
    val thongBaoLoi: String? = null,
    val successMessage: String? = null,
    val isLoading: Boolean = false
)
