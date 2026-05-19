package com.btl.buddybudget.data.db.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.btl.buddybudget.data.db.KieuGiaoDich

@Entity(tableName = "tbDanhMuc")
data class DanhMuc(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
        val iconName: String,
    val colorHex: String,
    val type: KieuGiaoDich = KieuGiaoDich.EXPENSE,
    /** Danh mục mặc định không cho phép xoá */
    val isDefault: Boolean = false
)