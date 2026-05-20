    package com.btl.buddybudget.data.db.entities

    import androidx.room.Entity
    import androidx.room.PrimaryKey

    @Entity(tableName = "tbVi")
    data class Vi(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val name: String,
        val soDuBanDau: Double = 0.0,
        val donVi: String = "VND",
        val iconName: String = "ti-wallet",
        val colorHex: String = "#0A5C32",
        /** Ẩn ví khỏi danh sách (thay vì xoá cứng) */
        val isArchived: Boolean = false,
        val sortOrder: Int = 0
    )