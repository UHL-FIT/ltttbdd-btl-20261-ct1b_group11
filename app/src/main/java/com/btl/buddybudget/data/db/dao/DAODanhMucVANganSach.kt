package com.btl.buddybudget.data.db.dao

import androidx.room.*
import com.btl.buddybudget.data.db.entities.DanhMuc
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

    @Query("SELECT * FROM tbDanhMuc WHERE name = :name LIMIT 1")
    suspend fun layTheoTen(name: String): DanhMuc?

    /** Dùng để kiểm tra seed dữ liệu */
    @Query("SELECT COUNT(*) FROM tbDanhMuc")
    suspend fun getCount(): Int
}


