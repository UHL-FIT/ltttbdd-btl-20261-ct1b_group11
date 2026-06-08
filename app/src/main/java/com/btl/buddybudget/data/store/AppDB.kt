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
    version = 4, // Nâng lên version 4 để đảm bảo Room xóa DB cũ và tạo lại theo cấu trúc mới
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
        DanhMuc(name = "Ăn uống",    iconName = "🍴", colorHex = "#FF6B35", type = KieuGiaoDich.EXPENSE, isDefault = true),
        DanhMuc(name = "Di chuyển",  iconName = "🚗", colorHex = "#3B82F6", type = KieuGiaoDich.EXPENSE, isDefault = true),
        DanhMuc(name = "Giải trí",   iconName = "🎮", colorHex = "#8B5CF6", type = KieuGiaoDich.EXPENSE, isDefault = true),
        DanhMuc(name = "Mua sắm",    iconName = "🛒", colorHex = "#10B981", type = KieuGiaoDich.EXPENSE, isDefault = true),
        DanhMuc(name = "Sức khoẻ",   iconName = "🏥", colorHex = "#EF4444", type = KieuGiaoDich.EXPENSE, isDefault = true),
        DanhMuc(name = "Giáo dục",   iconName = "🎓", colorHex = "#0EA5E9", type = KieuGiaoDich.EXPENSE, isDefault = true),
        DanhMuc(name = "Lương",      iconName = "💰", colorHex = "#22C55E", type = KieuGiaoDich.INCOME,  isDefault = true),
        DanhMuc(name = "Thưởng",     iconName = "🎁", colorHex = "#FBBF24", type = KieuGiaoDich.INCOME,  isDefault = true),
        DanhMuc(name = "Khác",       iconName = "📁", colorHex = "#94A3B8", type = KieuGiaoDich.EXPENSE, isDefault = true),
        DanhMuc(name = "Thay đổi số dư",       iconName = "💸", colorHex = "#94A3B8", type = KieuGiaoDich.INCOME, isDefault = true)
    ))
}

// Chỉ có ĐÚNG 1 hàm seedDefaultWallets ở đây
private suspend fun seedDefaultWallets(dao: DAOVi) {
    if (dao.layViChuaXoaCount() > 0) return
    dao.insertAll(listOf(
        Vi(name = "Tiền mặt",    iconName = "💵", colorHex = "#22C55E", sortOrder = 0),
        Vi(name = "Ngân hàng",   iconName = "🏦", colorHex = "#3B82F6", sortOrder = 1),
        Vi(name = "Ví điện tử",  iconName = "📱", colorHex = "#8B5CF6", sortOrder = 2)
    ))
}