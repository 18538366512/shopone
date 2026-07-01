package com.xby.shop624.util

import java.math.BigDecimal
import java.math.RoundingMode

object PriceUtil {

    fun format(value: Double): String {
        return BigDecimal.valueOf(value)
            .setScale(2, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
    }

    fun wholesaleRetailText(wholesale: String, retail: String): String {
        return "进货¥$wholesale  零售¥$retail"
    }
}
