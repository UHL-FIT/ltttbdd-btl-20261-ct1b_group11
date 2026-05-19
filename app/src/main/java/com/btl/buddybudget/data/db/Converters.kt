package com.btl.buddybudget.data.db

import androidx.room.TypeConverter

/**
 * Converters: chuyển đổi KieuGiaoDich <-> String để Room lưu vào SQLite.
 * Đăng ký trong @Database bằng @TypeConverters(Converters::class)
 */
class Converters {

    @TypeConverter
    fun tuKieuGiaoDich(kieu: KieuGiaoDich): String = kieu.name

    @TypeConverter
    fun thanhKieuGiaoDich(value: String): KieuGiaoDich =
        KieuGiaoDich.valueOf(value)
}