package com.xby.shop624.model

enum class OrderStatus(val label: String, val code: String) {
    PAID("待配送", "paid"),
    DELIVERING("配送中", "delivering"),
    COMPLETED("已完成", "completed");

    companion object {
        fun fromCode(code: String): OrderStatus {
            return entries.firstOrNull { it.code == code } ?: PAID
        }
    }
}
