package com.btl.buddybudget.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.btl.buddybudget.data.db.KieuGiaoDich

/**
 * Khoá ngoại:
 *   - categoryId → categories.id  (SET_DEFAULT khi xoá danh mục)
 *   - walletId   → wallets.id     (RESTRICT: không xoá ví khi còn giao dịch)
 */
@Entity(
    tableName = "tbGiaoDich",
    foreignKeys = [
        ForeignKey(
            entity = DanhMuc::class,
            parentColumns = ["id"],
            childColumns = ["idDanhMuc"],
            onDelete = ForeignKey.SET_DEFAULT
        ),
        ForeignKey(
            entity = Vi::class,
            parentColumns = ["id"],
            childColumns = ["idVi"],
            onDelete = ForeignKey.RESTRICT   // Bắt buộc xử lý giao dịch trước khi xoá ví
        )
    ],
    indices = [
        Index(value = ["idDanhMuc"]),
        Index(value = ["idVi"]),
        Index(value = ["date"]),
        Index(value = ["type"])
    ]
)
data class GiaoDich(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val idDanhMuc: Int = 9,
    val idVi: Int,
    val type: KieuGiaoDich,
    val date: Long = System.currentTimeMillis(),
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
