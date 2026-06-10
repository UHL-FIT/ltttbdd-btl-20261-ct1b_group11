    package com.btl.buddybudget.data.db.dao

    import androidx.room.*
    import  com.btl.buddybudget.data.db.entities.Vi
    import  com.btl.buddybudget.data.db.quanhe.WalletWithBalance
    import kotlinx.coroutines.flow.Flow

    @Dao
    interface DAOVi {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(vi: Vi): Long

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertAll(wallets: List<Vi>)

        @Update
        suspend fun update(vi: Vi)

        /** Không cho xoá cứng nếu còn giao dịch (RESTRICT ở FK).
         *  UI nên gọi canDelete() trước, nếu được thì mới gọi hàm này. */
        @Delete
        suspend fun delete(vi: Vi)

        @Query("SELECT * FROM tbVi WHERE isArchived = 0 ORDER BY sortOrder ASC, name ASC")
        fun layVicChuaXoa(): Flow<List<Vi>>

        @Query("SELECT * FROM tbVi ORDER BY sortOrder ASC, name ASC")
        fun layTatCaVi(): Flow<List<Vi>>

        @Query("SELECT * FROM tbVi WHERE id = :id LIMIT 1")
        suspend fun layViTheoID(id: Int): Vi?

        /** Số dư hiện tại = thu - chi */
        @Query("""
            SELECT
                vi.id,
                vi.name,
                vi.iconName,
                vi.colorHex,
                vi.donVi,
                vi.isArchived,
                vi.sortOrder,
                COALESCE(
                      SUM(CASE WHEN gd.type = 'INCOME'  THEN  gd.amount ELSE 0 END)
                    - SUM(CASE WHEN gd.type = 'EXPENSE' THEN  gd.amount ELSE 0 END),
                    0.0
                ) 
                AS soDuHienTai,
                COUNT(gd.id) AS transactionCount
            FROM tbVi vi
            LEFT JOIN tbGiaoDich gd ON gd.idVi = vi.id
            GROUP BY vi.id
            ORDER BY vi.sortOrder ASC
        """)
        fun layWalletsWithBalance(): Flow<List<WalletWithBalance>>

        /** Tổng tài sản tất cả ví đang hoạt động */
        @Query("""
            SELECT COALESCE(
                  SUM(CASE WHEN gd.type = 'INCOME'  THEN  gd.amount ELSE 0 END)
                - SUM(CASE WHEN gd.type = 'EXPENSE' THEN  gd.amount ELSE 0 END),
                0.0
            )
            FROM tbVi vi
            LEFT JOIN tbGiaoDich gd ON gd.idVi = vi.id
            WHERE vi.isArchived = 0
        """)
        fun layTongTienCacVi(): Flow<Double>

        /** Kiểm tra ví có giao dịch nào không — dùng trước khi cho phép xoá */
        @Query("SELECT COUNT(*) FROM tbGiaoDich WHERE idVi = :idvi")
        suspend fun demSoGiaoDich(idvi: Int): Int

        @Query("SELECT COUNT(*) FROM tbVi WHERE isArchived = 0")
        suspend fun layViChuaXoaCount(): Int

        @Query("SELECT * FROM tbVi")
        suspend fun getAllWalletsStatic(): List<Vi>

        @Query("DELETE FROM tbVi")
        suspend fun deleteAll()
    }