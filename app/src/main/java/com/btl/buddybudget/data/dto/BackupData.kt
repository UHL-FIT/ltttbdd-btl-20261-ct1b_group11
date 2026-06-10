package com.btl.buddybudget.data.dto

import com.btl.buddybudget.data.db.entities.DanhMuc
import com.btl.buddybudget.data.db.entities.GiaoDich
import com.btl.buddybudget.data.db.entities.Vi

data class BackupData(
    val version: Int = 1,
    val exportDate: Long = System.currentTimeMillis(),
    val wallets: List<Vi>,
    val categories: List<DanhMuc>,
    val transactions: List<GiaoDich>
)