package com.btl.buddybudget.ui.vi

import com.btl.buddybudget.data.db.quanhe.WalletWithBalance

data class ViScreenState(

    val wallets: List<WalletWithBalance> = emptyList(),

    val tongTaiSan: Double = 0.0,

    val isLoading: Boolean = false

)