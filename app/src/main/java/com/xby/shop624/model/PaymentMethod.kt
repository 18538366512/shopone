package com.xby.shop624.model

enum class PaymentMethod(val label: String, val code: String) {
    BALANCE("账户余额", "balance"),
    ALIPAY("支付宝", "alipay"),
    WECHAT("微信支付", "wechat"),
    COD("货到付款", "cod");

    companion object {
        fun fromCode(code: String): PaymentMethod {
            return entries.firstOrNull { it.code == code } ?: BALANCE
        }
    }
}
