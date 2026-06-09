package com.btl.buddybudget.data.db.dao

import androidx.room.*
import com.btl.buddybudget.data.db.entities.GiaoDich
import com.btl.buddybudget.data.db.quanhe.GiaoDichvaDanhMuc
import com.btl.buddybudget.data.db.quanhe.ThongKeDanhMuc
import kotlinx.coroutines.flow.Flow

@Dao
interface DAOGiaoDich {
    @Insert
    suspend fun them(giaoDich: GiaoDich): Long

    @Update
    suspend fun sua(giaoDich: GiaoDich)

    @Delete
    suspend fun xoa(giaoDich: GiaoDich)

    @Query("DELETE FROM tbGiaoDich WHERE id = :id")
    suspend fun xoaTheoId(id: Int)

    @Query("SELECT * FROM tbGiaoDich WHERE id = :id")
    suspend fun layTheoId(id: Int): GiaoDich?

    @Transaction
    @Query("""
        SELECT g.* FROM tbGiaoDich g
        INNER JOIN tbVi v ON g.idVi = v.id
        WHERE v.isArchived = 0
        ORDER BY g.date DESC
    """)
    fun layTatCa(): Flow<List<GiaoDichvaDanhMuc>>

    @Transaction
    @Query("""
        SELECT g.* FROM tbGiaoDich g
        INNER JOIN tbVi v ON g.idVi = v.id
        WHERE v.isArchived = 0 AND g.date >= :tu AND g.date < :den 
        ORDER BY g.date DESC
    """)
    fun layTheoKhoangThoiGian(tu: Long, den: Long): Flow<List<GiaoDichvaDanhMuc>>

    @Transaction
    @Query("SELECT * FROM tbGiaoDich WHERE idVi = :idVi ORDER BY date DESC")
    fun layTheoVi(idVi: Int): Flow<List<GiaoDichvaDanhMuc>>

    @Transaction
    @Query("SELECT * FROM tbGiaoDich WHERE idVi = :idVi AND date >= :tu AND date < :den ORDER BY date DESC")
    fun layTheoViVaThang(idVi: Int, tu: Long, den: Long): Flow<List<GiaoDichvaDanhMuc>>

    @Transaction
    @Query("""
        SELECT g.* FROM tbGiaoDich g
        INNER JOIN tbVi v ON g.idVi = v.id
        WHERE v.isArchived = 0 AND g.note LIKE '%' || :tuKhoa || '%' 
        ORDER BY g.date DESC
    """)
    fun timKiem(tuKhoa: String): Flow<List<GiaoDichvaDanhMuc>>

    @Query("""
        SELECT SUM(g.amount) FROM tbGiaoDich g
        INNER JOIN tbVi v ON g.idVi = v.id
        WHERE v.isArchived = 0 AND g.type = :type AND g.date >= :tu AND g.date < :den
    """)
    fun tinhTong(type: String, tu: Long, den: Long): Flow<Double>

    @Query("""
        SELECT MIN(g.amount) FROM tbGiaoDich g
        INNER JOIN tbVi v ON g.idVi = v.id
        WHERE v.isArchived = 0 AND g.type = :type AND g.date >= :tu AND g.date < :den
    """)
    fun tinhMin(type: String, tu: Long, den: Long): Flow<Double>

    @Query("""
        SELECT MAX(g.amount) FROM tbGiaoDich g
        INNER JOIN tbVi v ON g.idVi = v.id
        WHERE v.isArchived = 0 AND g.type = :type AND g.date >= :tu AND g.date < :den
    """)
    fun tinhMax(type: String, tu: Long, den: Long): Flow<Double>

    @Query("""
        SELECT AVG(g.amount) FROM tbGiaoDich g
        INNER JOIN tbVi v ON g.idVi = v.id
        WHERE v.isArchived = 0 AND g.type = :type AND g.date >= :tu AND g.date < :den
    """)
    fun tinhTrungBinh(type: String, tu: Long, den: Long): Flow<Double>

    @Query("""
        SELECT COUNT(g.id) FROM tbGiaoDich g
        INNER JOIN tbVi v ON g.idVi = v.id
        WHERE v.isArchived = 0
    """)
    fun demTatCa(): Flow<Int>

    @Query("""
        SELECT d.id, d.name, d.colorHex, SUM(g.amount) as total, COUNT(g.id) as count
        FROM tbDanhMuc d
        JOIN tbGiaoDich g ON d.id = g.idDanhMuc
        JOIN tbVi v ON g.idVi = v.id
        WHERE v.isArchived = 0 AND g.date >= :tu AND g.date < :den
        GROUP BY d.id
    """)
    fun thongKeDanhMuc(tu: Long, den: Long): Flow<List<ThongKeDanhMuc>>
}