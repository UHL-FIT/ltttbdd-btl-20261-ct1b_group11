package com.btl.buddybudget.ui.vi

data class SuaViState(
    val id: Int = 0,
    val name: String = "",
   //val soDu: String = "",
    val donVi: String = "VND",
    val iconName: String = "ti-wallet",
    val colorHex: String = "#4CAF50",
    val isArchived: Boolean = false,
    val sortOrder: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)