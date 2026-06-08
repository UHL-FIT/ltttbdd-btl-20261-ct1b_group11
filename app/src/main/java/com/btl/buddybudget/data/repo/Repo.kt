package com.btl.buddybudget.data.repo

import com.btl.buddybudget.data.db.KieuGiaoDich
import com.btl.buddybudget.data.db.dao.DAODanhMuc
import com.btl.buddybudget.data.db.dao.DAOGiaoDich
import com.btl.buddybudget.data.db.dao.DAOVi
import com.btl.buddybudget.data.db.entities.DanhMuc
import com.btl.buddybudget.data.db.entities.GiaoDich
import com.btl.buddybudget.data.db.entities.Vi
import com.btl.buddybudget.data.db.quanhe.ThongKeDanhMuc
import com.btl.buddybudget.data.db.quanhe.GiaoDichvaDanhMuc
import com.btl.buddybudget.data.db.quanhe.WalletWithBalance
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
class Repo   @Inject constructor(
    private val daoGiaoDich: DAOGiaoDich,
    private val daoDanhMuc:  DAODanhMuc,
    private val daoVi:       DAOVi
) {
    // ════════════════════════════════════════════════════════════════
    //  GIAO DỊCH
    // ════════════════════════════════════════════════════════════════

    suspend fun themGiaoDich(giaoDich: GiaoDich): Long =
        daoGiaoDich.them(giaoDich)

    suspend fun suaGiaoDich(giaoDich: GiaoDich) =
        daoGiaoDich.sua(giaoDich)

    suspend fun xoaGiaoDich(giaoDich: GiaoDich) =
        daoGiaoDich.xoa(giaoDich)

    suspend fun xoaGiaoDichTheoId(id: Int) =
        daoGiaoDich.xoaTheoId(id)

    suspend fun layGiaoDichTheoId(id: Int): GiaoDich? =
        daoGiaoDich.layTheoId(id)

    /** Tất cả giao dịch kèm DanhMuc + Vi */
    fun layTatCaGiaoDich(): Flow<List<GiaoDichvaDanhMuc>> =
        daoGiaoDich.layTatCa()

    /** Giao dịch trong một tháng cụ thể */
    fun layGiaoDichTheoThang(thang: Int, nam: Int): Flow<List<GiaoDichvaDanhMuc>> {
        val (tu, den) = khoangThang(thang, nam)
        return daoGiaoDich.layTheoKhoangThoiGian(tu, den)
    }

    /** Giao dịch theo ví */
    fun layGiaoDichTheoVi(idVi: Int): Flow<List<GiaoDichvaDanhMuc>> =
        daoGiaoDich.layTheoVi(idVi)

    /** Giao dịch theo ví trong một tháng */
    fun layGiaoDichTheoViVaThang(idVi: Int, thang: Int, nam: Int): Flow<List<GiaoDichvaDanhMuc>> {
        val (tu, den) = khoangThang(thang, nam)
        return daoGiaoDich.layTheoViVaThang(idVi, tu, den)
    }

    fun timKiemGiaoDich(tuKhoa: String): Flow<List<GiaoDichvaDanhMuc>> =
        daoGiaoDich.timKiem(tuKhoa)

    // ════════════════════════════════════════════════════════════════
    //  THỐNG KÊ
    // ════════════════════════════════════════════════════════════════

    fun tinhTongThu(thang: Int, nam: Int): Flow<Double> {
        val (tu, den) = khoangThang(thang, nam)
        return daoGiaoDich.tinhTong(KieuGiaoDich.INCOME.name, tu, den)
    }

    fun tinhTongChi(thang: Int, nam: Int): Flow<Double> {
        val (tu, den) = khoangThang(thang, nam)
        return daoGiaoDich.tinhTong(KieuGiaoDich.EXPENSE.name, tu, den)
    }

    fun tinhChiNhoNhat(thang: Int, nam: Int): Flow<Double> {
        val (tu, den) = khoangThang(thang, nam)
        return daoGiaoDich.tinhMin(KieuGiaoDich.EXPENSE.name, tu, den)
    }

    fun tinhChiLonNhat(thang: Int, nam: Int): Flow<Double> {
        val (tu, den) = khoangThang(thang, nam)
        return daoGiaoDich.tinhMax(KieuGiaoDich.EXPENSE.name, tu, den)
    }

    fun tinhChiTrungBinh(thang: Int, nam: Int): Flow<Double> {
        val (tu, den) = khoangThang(thang, nam)
        return daoGiaoDich.tinhTrungBinh(KieuGiaoDich.EXPENSE.name, tu, den)
    }

    fun demTatCaGiaoDich(): Flow<Int> =
        daoGiaoDich.demTatCa()

    /** Thống kê chi theo danh mục — dùng cho biểu đồ tròn */
    fun thongKeDanhMuc(thang: Int, nam: Int): Flow<List<ThongKeDanhMuc>> {
        val (tu, den) = khoangThang(thang, nam)
        return daoGiaoDich.thongKeDanhMuc(tu, den)
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

    suspend fun layTheoTen(name: String): DanhMuc? =
        daoDanhMuc.layTheoTen(name)

    fun layDanhMucChi(): Flow<List<DanhMuc>> =
        daoDanhMuc.layDanhMucTheoLoai(KieuGiaoDich.EXPENSE.name)

    fun layDanhMucThu(): Flow<List<DanhMuc>> =
        daoDanhMuc.layDanhMucTheoLoai(KieuGiaoDich.INCOME.name)


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

    /** Archive ví thay vì xoá — giữ lại lịch sử giao dịch */
    suspend fun anVi(vi: Vi) =
        daoVi.update(vi.copy(isArchived = true))

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