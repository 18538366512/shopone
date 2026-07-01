package com.xby.shop624.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xby.shop624.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE phone = :phone AND password = :password LIMIT 1")
    suspend fun login(phone: String, password: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int

    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    suspend fun getByPhone(phone: String): UserEntity?

    @Query("UPDATE users SET balance = :balance WHERE phone = :phone")
    suspend fun updateBalance(phone: String, balance: Double)

    @Query("UPDATE users SET nickname = :nickname WHERE phone = :phone")
    suspend fun updateNickname(phone: String, nickname: String)

    @Query("UPDATE users SET shopName = :shopName WHERE phone = :phone")
    suspend fun updateShopName(phone: String, shopName: String)

    @Query("UPDATE users SET shopAddress = :shopAddress WHERE phone = :phone")
    suspend fun updateShopAddress(phone: String, shopAddress: String)
}
