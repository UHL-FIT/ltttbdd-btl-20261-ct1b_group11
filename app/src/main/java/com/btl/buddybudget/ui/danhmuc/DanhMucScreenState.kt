package com.btl.buddybudget.ui.danhmuc

import com.btl.buddybudget.data.db.entities.DanhMuc

data class DanhMucScreenState(
    val danhMucs: List<DanhMuc> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterType: String? = null
)