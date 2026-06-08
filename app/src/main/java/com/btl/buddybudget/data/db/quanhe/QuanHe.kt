package com.btl.buddybudget.data.db.quanhe

import androidx.room.Embedded
import androidx.room.Relation
import com.btl.buddybudget.data.db.entities.DanhMuc
import com.btl.buddybudget.data.db.entities.GiaoDich
import com.btl.buddybudget.data.db.entities.Vi

data class GiaoDichvaDanhMuc(
    @Embedded val giaodich: GiaoDich,
    @Relation(parentColumn = "idDanhMuc", entityColumn = "id")
    val danhmuc: DanhMuc?,
    @Relation(parentColumn = "idVi",   entityColumn = "id")
    val vi: Vi?
)


data class WalletWithBalance(
    val id: Int,
    val name: String,
    val iconName: String,
    val colorHex: String,
    val donVi: String,
    val isArchived: Boolean,
    val sortOrder: Int,
    val soDuHienTai: Double,
    val transactionCount: Int
)

data class ThongKeDanhMuc(
    val id: Int,
    val name: String,
    val colorHex: String,
    val total: Double,
    val count: Int
)
