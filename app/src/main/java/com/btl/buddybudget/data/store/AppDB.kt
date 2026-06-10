package com.btl.buddybudget.data.store

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.btl.buddybudget.data.db.Converters
import com.btl.buddybudget.data.db.KieuGiaoDich
import com.btl.buddybudget.data.db.dao.*
import com.btl.buddybudget.data.db.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        GiaoDich::class,
        DanhMuc::class,
        Vi::class
    ],
    version = 4, //
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun daoGiaoDich(): DAOGiaoDich
    abstract fun daoDanhMuc(): DAODanhMuc

    abstract fun daoVi(): DAOVi

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun layDataBase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_buddy.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build().also { INSTANCE = it }
            }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    seedDefaultCategories(database.daoDanhMuc())
                    seedDefaultWallets(database.daoVi())
                }
            }
        }
    }
}

// Chỉ có ĐÚNG 1 hàm seedDefaultCategories ở đây
private suspend fun seedDefaultCategories(dao: DAODanhMuc) {
    if (dao.getCount() > 0) return
    dao.insertAll(listOf(
        DanhMuc(name = "Ăn uống",    iconName = "🍴", colorHex = "#F44336", type = KieuGiaoDich.EXPENSE, isDefault = true), // Đỏ
        DanhMuc(name = "Di chuyển",  iconName = "🚗", colorHex = "#2196F3", type = KieuGiaoDich.EXPENSE, isDefault = true), // Xanh lam
        DanhMuc(name = "Giải trí",   iconName = "🎮", colorHex = "#9C27B0", type = KieuGiaoDich.EXPENSE, isDefault = true), // Tím
        DanhMuc(name = "Mua sắm",    iconName = "🛒", colorHex = "#FF9800", type = KieuGiaoDich.EXPENSE, isDefault = true), // Cam
        DanhMuc(name = "Sức khoẻ",   iconName = "🏥", colorHex = "#E91E63", type = KieuGiaoDich.EXPENSE, isDefault = true), // Hồng
        DanhMuc(name = "Giáo dục",   iconName = "🎓", colorHex = "#00BCD4", type = KieuGiaoDich.EXPENSE, isDefault = true), // Xanh lơ (Cyan)
        DanhMuc(name = "Khác",       iconName = "📁", colorHex = "#607D8B", type = KieuGiaoDich.EXPENSE, isDefault = true), // Xám xanh

        // KHOẢN THU (INCOME)
        DanhMuc(name = "Lương",      iconName = "💰", colorHex = "#4CAF50", type = KieuGiaoDich.INCOME,  isDefault = true), // Xanh lá
        DanhMuc(name = "Thưởng",     iconName = "🎁", colorHex = "#FFEB3B", type = KieuGiaoDich.INCOME,  isDefault = true), // Vàng
        DanhMuc(name = "Thay đổi số dư", iconName = "💸", colorHex = "#795548", type = KieuGiaoDich.INCOME, isDefault = true)  // Nâu
    ))
}

// Chỉ có ĐÚNG 1 hàm seedDefaultWallets ở đây
private suspend fun seedDefaultWallets(dao: DAOVi) {
    if (dao.layViChuaXoaCount() > 0) return
    dao.insertAll(listOf(
        Vi(name = "Tiền mặt",    iconName = "💵", colorHex = "#4CAF50", sortOrder = 0),
        Vi(name = "Ngân hàng",   iconName = "🏦", colorHex = "#2196F3", sortOrder = 1),
        Vi(name = "Ví điện tử",  iconName = "📱", colorHex = "#9C27B0", sortOrder = 2)
    ))
}
