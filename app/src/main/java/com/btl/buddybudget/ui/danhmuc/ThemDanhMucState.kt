package com.btl.buddybudget.ui.danhmuc

import com.btl.buddybudget.data.db.KieuGiaoDich
import com.btl.buddybudget.data.icon.TongHopIcon

data class ThemDanhMucState(
    val tenDanhMuc: String = "",
    val loaiGiaoDich: KieuGiaoDich = KieuGiaoDich.EXPENSE,
    // Lấy phần tử đầu tiên làm mặc định
    val iconChon: String = TongHopIcon.DanhSachIconChi[0],
    val mauChon: String = TongHopIcon.DanhSachMau[0],
    val daLuuThanhCong: Boolean = false,
    val thongBaoLoi: String? = null
)