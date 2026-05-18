package com.btl.buddybudget.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Ràng buộc: mỗi (categoryId, month, year) là duy nhất.
 * ForeignKey -> Category (CASCADE khi xoá danh mục)
 **/
@Entity(
    tableName = "tbNganSach",
    foreignKeys = [
        ForeignKey(
            entity = DanhMuc::class,
            parentColumns = ["id"],
            childColumns = ["idDanhMuc"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idDanhMuc", "month", "year"], unique = true)
    ]
)
data class NganSach(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idDanhMuc: Int,
    val limitAmount: Double,
    val month: Int,
    val year: Int
)
