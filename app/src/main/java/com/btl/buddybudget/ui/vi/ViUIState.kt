package com.btl.buddybudget.ui.vi


import com.btl.buddybudget.data.icon.TongHopIcon


data class ViUIState(

    val name: String = "",

    val soDu: String = "",

    val donVi: String = "VND",

    val iconName: String = TongHopIcon.DanhSachIconVi[0],

    val colorHex: String = TongHopIcon.DanhSachMau[0],

    val isArchived: Boolean = false,

    val enableNotification: Boolean = false,

    val isLoading: Boolean = false,

    val error: String? = null
)