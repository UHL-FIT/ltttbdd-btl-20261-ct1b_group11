package com.btl.buddybudget.data.repo

import com.btl.buddybudget.data.db.KieuGiaoDich
import com.btl.buddybudget.data.db.dao.DAODanhMuc
import com.btl.buddybudget.data.db.dao.DAOGiaoDich
import com.btl.buddybudget.data.db.dao.DAOVi
import com.btl.buddybudget.data.db.entities.DanhMuc
import com.btl.buddybudget.data.db.entities.GiaoDich
import com.btl.buddybudget.data.db.entities.Vi
import com.btl.buddybudget.data.db.quanhe.ThongKeDanhMuc
import com.btl.buddybudget.data.db.quanhe.GiaoDichvaDanhMucvaVi
import com.btl.buddybudget.data.db.quanhe.WalletWithBalance
import com.btl.buddybudget.data.dto.BackupData
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * BudgetRepository — Single Source of Truth cho toàn bộ dữ liệu.
 *
 * ViewModel KHÔNG gọi DAO trực tiếp; chỉ giao tiếp qua Repository.
 * Mọi logic tính toán ngày tháng đặt ở đây (không để trong DAO hay ViewModel).
 *
 * Sử dụng @Inject nếu dùng Hilt; nếu không dùng DI thì khởi tạo thủ công
 * trong Application class và truyền vào ViewModel qua ViewModelFactory.
 */
@Singleton
class Repo @Inject constructor(
    private val database: com.btl.buddybudget.data.store.AppDatabase,
    private val daoGiaoDich: DAOGiaoDich,
    private val daoDanhMuc:  DAODanhMuc,
    private val daoVi:       DAOVi
) {
    // ════════════════════════════════════════════════════════════════
    //  BACKUP & RESTORE (Dữ liệu JSON)
    // ════════════════════════════════════════════════════════════════

    suspend fun getBackupData(): BackupData {
        return BackupData(
            wallets = daoVi.getAllWalletsStatic(),
            categories = daoDanhMuc.getAllCategoriesStatic(),
            transactions = daoGiaoDich.getAllTransactionsStatic()
        )
    }

    suspend fun restoreBackupData(backup: BackupData) {
        database.withTransaction {
            // Xóa theo thứ tự để tránh lỗi Foreign Key
            daoGiaoDich.deleteAll()
            daoDanhMuc.deleteAll()
            daoVi.deleteAll()

            // Chèn lại dữ liệu
            daoVi.insertAll(backup.wallets)
            daoDanhMuc.insertAll(backup.categories)
            daoGiaoDich.insertAll(backup.transactions)
        }
    }
    // ════════════════════════════════════════════════════════════════
    //  GIAO DỊCH
    // ════════════════════════════════════════════════════════════════

    suspend fun themGiaoDich(giaoDich: GiaoDich): Long =
        daoGiaoDich.them(giaoDich)

    suspend fun suaGiaoDich(giaoDich: GiaoDich) =
        daoGiaoDich.sua(giaoDich)

    suspend fun xoaGiaoDich(giaoDich: GiaoDich) =
        daoGiaoDich.xoa(giaoDich)



    suspend fun layGiaoDichTheoId(id: Int): GiaoDich? =
        daoGiaoDich.layTheoId(id)



    /** Giao dịch trong một tháng cụ thể */
    fun layGiaoDichTheoThang(thang: Int, nam: Int): Flow<List<GiaoDichvaDanhMucvaVi>> {
        val (tu, den) = khoangThang(thang, nam)
        return daoGiaoDich.layTheoKhoangThoiGian(tu, den)
    }

    /** Giao dịch theo ví trong một tháng */
    fun layGiaoDichTheoViVaThang(idVi: Int, thang: Int, nam: Int): Flow<List<GiaoDichvaDanhMucvaVi>> {
        val (tu, den) = khoangThang(thang, nam)
        return daoGiaoDich.layTheoViVaThang(idVi, tu, den)
    }

    fun timKiemGiaoDich(tuKhoa: String): Flow<List<GiaoDichvaDanhMucvaVi>> =
        daoGiaoDich.timKiem(tuKhoa)

    // ════════════════════════════════════════════════════════════════
    //  THỐNG KÊ
    // ════════════════════════════════════════════════════════════════


    /** Thống kê theo danh mục — dùng cho biểu đồ tròn */
    fun thongKeDanhMuc(thang: Int, nam: Int, type: String): Flow<List<ThongKeDanhMuc>> {
        val (tu, den) = khoangThang(thang, nam)
        return daoGiaoDich.thongKeDanhMuc(type, tu, den)
    }

    // ════════════════════════════════════════════════════════════════
    //  DANH MỤC
    // ════════════════════════════════════════════════════════════════

    suspend fun themDanhMuc(danhMuc: DanhMuc): Long =
        daoDanhMuc.insert(danhMuc)

    suspend fun suaDanhMuc(danhMuc: DanhMuc) =
        daoDanhMuc.update(danhMuc)

    suspend fun xoaDanhMuc(danhMuc: DanhMuc) =
        daoDanhMuc.delete(danhMuc)

    fun layTatCaDanhMuc(): Flow<List<DanhMuc>> =
        daoDanhMuc.layTatCa()

    suspend fun layDanhMucTheoId(id: Int): DanhMuc? =
        daoDanhMuc.layDanhMucTheoID(id)

    suspend fun coGiaoDichTrongDanhMuc(idDanhMuc: Int): Boolean =
        daoGiaoDich.demGiaoDichTheoDanhMuc(idDanhMuc) > 0

    suspend fun layDanhMucTheoTen(name: String): DanhMuc? =
        daoDanhMuc.layTheoTen(name)



    // ════════════════════════════════════════════════════════════════
    //  VÍ
    // ════════════════════════════════════════════════════════════════

    suspend fun themVi(vi: Vi): Long =
        daoVi.insert(vi)

    suspend fun suaVi(vi: Vi) =
        daoVi.update(vi)

    /**
     * Xoá ví — chỉ được khi không còn giao dịch nào.
     * Trả về true nếu xoá thành công, false nếu còn giao dịch.
     */
    suspend fun xoaVi(vi: Vi): Boolean {
        if (daoVi.demSoGiaoDich(vi.id) > 0) return false
        daoVi.delete(vi)
        return true
    }


    fun layViDangDung(): Flow<List<Vi>> =
        daoVi.layVicChuaXoa()

    /** Ví kèm số dư tính toán — dùng cho màn hình Home */
    fun layViVaSoDu(): Flow<List<WalletWithBalance>> =
        daoVi.layWalletsWithBalance()

    /** Tổng tài sản ròng tất cả ví */
    fun layTongTaiSan(): Flow<Double> =
        daoVi.layTongTienCacVi()

    suspend fun layViTheoId(id: Int): Vi? =
        daoVi.layViTheoID(id)

    suspend fun layViTheoTen(name: String): Vi? =
        daoVi.layTheoTen(name)

    /** Kiểm tra ví có thể xoá không */
    suspend fun coTheXoaVi(idVi: Int): Boolean =
        daoVi.demSoGiaoDich(idVi) == 0

    // ════════════════════════════════════════════════════════════════
    //  HÀM HỖ TRỢ
    // ════════════════════════════════════════════════════════════════

    /**
     * Tính epoch ms đầu và cuối tháng (exclusive end).
     *
     * khoangThang(5, 2026):
     *   tu  = 2026-05-01 00:00:00.000
     *   den = 2026-06-01 00:00:00.000
     *
     * Query dùng: WHERE date >= tu AND date < den
     */
    private fun khoangThang(thang: Int, nam: Int): Pair<Long, Long> {
        val cal = Calendar.getInstance().apply {
            set(nam, thang - 1, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val tu = cal.timeInMillis
        cal.add(Calendar.MONTH, 1)
        return tu to cal.timeInMillis
    }
}