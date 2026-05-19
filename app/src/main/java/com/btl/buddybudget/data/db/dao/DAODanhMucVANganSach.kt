package com.btl.buddybudget.data.db.dao

import androidx.room.*
import com.btl.buddybudget.data.db.entities.NganSach
import com.btl.buddybudget.data.db.entities.DanhMuc
import com.btl.buddybudget.data.db.quanhe.NganSachVADanhMuc
import kotlinx.coroutines.flow.Flow


@Dao
interface DAODanhMuc {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: DanhMuc): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(categories: List<DanhMuc>)

    @Update
    suspend fun update(category: DanhMuc)

    @Delete
    suspend fun delete(category: DanhMuc)

    /** Lấy tất cả danh mục, sắp xếp theo tên */
    @Query("SELECT * FROM tbDanhMuc ORDER BY name ASC")
    fun layTatCa(): Flow<List<DanhMuc>>

    /** Lấy danh mục theo loại (INCOME / EXPENSE) */
    @Query("SELECT * FROM tbDanhMuc WHERE type = :type ORDER BY name ASC")
    fun layDanhMucTheoLoai(type: String): Flow<List<DanhMuc>>

    @Query("SELECT * FROM tbDanhMuc WHERE id = :id LIMIT 1")
    suspend fun layDanhMucTheoID(id: Int): DanhMuc?

    /** Dùng để kiểm tra seed dữ liệu */
    @Query("SELECT COUNT(*) FROM tbDanhMuc")
    suspend fun getCount(): Int
}


@Dao
interface DAONganSach {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: NganSach): Long

    @Update
    suspend fun update(budget: NganSach)

    @Delete
    suspend fun delete(budget: NganSach)

    /**
     * Lấy tất cả ngân sách trong tháng/năm,
     * kèm thông tin danh mục để hiển thị.
     */
    @Transaction
    @Query("""
        SELECT * FROM tbNganSach
        WHERE month = :month AND year = :year
        ORDER BY idDanhMuc ASC
    """)
    fun layNganSachTheoThangNamKemDanhMuc(month: Int, year: Int): Flow<List<NganSachVADanhMuc>>

    /** Lấy ngân sách của 1 danh mục cụ thể trong tháng */
    @Query("""
        SELECT * FROM tbNganSach
        WHERE idDanhMuc = :iddanhmuc AND month = :month AND year = :year
        LIMIT 1
    """)
    suspend fun layNganSachTheoThangKemDanhMuc(
        iddanhmuc: Int,
        month: Int,
        year: Int
    ): NganSach?

    /** Upsert: nếu đã có thì cập nhật, chưa có thì thêm mới */
    @Query("""
        INSERT OR REPLACE INTO tbNganSach (id, idDanhMuc, limitAmount, month, year)
        VALUES (
            COALESCE(
                (SELECT id FROM tbNganSach WHERE idDanhMuc = :iddanhmuc AND month = :month AND year = :year),
                NULL
            ),
            :iddanhmuc, :limitAmount, :month, :year
        )
    """)
    suspend fun upsert(iddanhmuc: Int, limitAmount: Double, month: Int, year: Int)

    @Query("DELETE FROM tbNganSach WHERE idDanhMuc = :iddanhmuc AND month = :month AND year = :year")
    suspend fun XoaNganSachTheoThangKemDanhMuc(iddanhmuc: Int, month: Int, year: Int)

    /** Tổng số ngân sách đang thiết lập */
    @Query("SELECT COUNT(*) FROM tbNganSach WHERE month = :month AND year = :year")
    fun getCountByMonth(month: Int, year: Int): Flow<Int>
}