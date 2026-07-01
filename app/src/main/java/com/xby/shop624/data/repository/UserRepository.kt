package com.xby.shop624.data.repository

import com.xby.shop624.data.local.dao.UserDao
import com.xby.shop624.data.local.entity.UserEntity

class UserRepository(private val userDao: UserDao) {

    suspend fun login(phone: String, password: String): UserEntity? {
        return userDao.login(phone, password)
    }

    suspend fun register(phone: String, password: String): Result<UserEntity> {
        if (phone.length != 11) {
            return Result.failure(IllegalArgumentException("请输入11位手机号"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("密码至少6位"))
        }
        val user = UserEntity(phone = phone, password = password)
        userDao.insert(user)
        return Result.success(user)
    }

    suspend fun getUser(phone: String): UserEntity? = userDao.getByPhone(phone)

    suspend fun updateNickname(phone: String, nickname: String): Result<UserEntity> {
        if (nickname.isBlank()) return Result.failure(IllegalArgumentException("姓名不能为空"))
        userDao.updateNickname(phone, nickname.trim())
        return Result.success(userDao.getByPhone(phone)!!)
    }

    suspend fun updateShopName(phone: String, shopName: String): Result<UserEntity> {
        if (shopName.isBlank()) return Result.failure(IllegalArgumentException("店铺名不能为空"))
        userDao.updateShopName(phone, shopName.trim())
        return Result.success(userDao.getByPhone(phone)!!)
    }

    suspend fun updateShopAddress(phone: String, address: String): Result<UserEntity> {
        if (address.isBlank()) return Result.failure(IllegalArgumentException("地址不能为空"))
        userDao.updateShopAddress(phone, address.trim())
        return Result.success(userDao.getByPhone(phone)!!)
    }

    suspend fun deductBalance(phone: String, amount: Double): Result<Double> {
        val user = userDao.getByPhone(phone) ?: return Result.failure(IllegalStateException("用户不存在"))
        if (user.balance < amount) {
            return Result.failure(IllegalArgumentException("账户余额不足"))
        }
        val newBalance = user.balance - amount
        userDao.updateBalance(phone, newBalance)
        return Result.success(newBalance)
    }

    suspend fun ensureDefaultUser() {
        if (userDao.getUserCount() == 0) {
            userDao.insert(
                UserEntity(
                    phone = "13800138000",
                    password = "123456",
                    nickname = "张老板",
                    shopName = "幸福社区便利店",
                    shopAddress = "幸福路88号",
                    balance = 500.0
                )
            )
        }
    }
}
