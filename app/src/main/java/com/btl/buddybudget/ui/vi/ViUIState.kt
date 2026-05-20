package com.btl.buddybudget.ui.vi


data class ViUIState(

    val name: String = "",

    val soDu: String = "",

    val donVi: String = "VND",

    val iconName: String = "ti-wallet",

    val colorHex: String = "#4CAF50",

    val isArchived: Boolean = false,

    val enableNotification: Boolean = false,

    val isLoading: Boolean = false,

    val error: String? = null
)