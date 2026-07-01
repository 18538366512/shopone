package com.xby.shop624.data.repository

import com.xby.shop624.data.local.dao.BannerDao
import com.xby.shop624.data.local.dao.NavCategoryDao
import com.xby.shop624.data.local.dao.ProductDao
import com.xby.shop624.data.local.entity.BannerEntity
import com.xby.shop624.data.local.entity.NavCategoryEntity
import com.xby.shop624.data.local.entity.ProductEntity

class HomeRepository(
    private val bannerDao: BannerDao,
    private val navCategoryDao: NavCategoryDao,
    private val productDao: ProductDao
) {

    suspend fun getBanners(): List<BannerEntity> = bannerDao.getAll()

    suspend fun getNavCategories(): List<NavCategoryEntity> = navCategoryDao.getAll()

    suspend fun getProducts(categoryKey: String?): List<ProductEntity> {
        val key = categoryKey?.takeIf { it != CATEGORY_ALL }
        return productDao.getByCategory(key)
    }

    suspend fun searchProducts(keyword: String, categoryKey: String? = null): List<ProductEntity> {
        val key = categoryKey?.takeIf { it != CATEGORY_ALL }
        return productDao.search(keyword.trim(), key)
    }

    suspend fun ensureDefaultData() {
        if (bannerDao.getCount() == 0) {
            bannerDao.insertAll(defaultBanners())
        }
        if (navCategoryDao.getCount() == 0) {
            navCategoryDao.insertAll(defaultNavCategories())
        }
        if (productDao.getCount() == 0) {
            productDao.insertAll(defaultProducts())
        }
    }

    companion object {
        const val CATEGORY_ALL = "all"

        private fun defaultBanners() = listOf(
            BannerEntity(1, "便利店进货", "次日配送到店 省时省力", "#FF6B00", "drawable://banner_store", 1),
            BannerEntity(2, "酒水饮料", "农夫山泉 雪花 娃哈哈", "#4CAF50", "drawable://banner_drink", 2),
            BannerEntity(3, "休闲零食", "薯片 饼干 糖果 坚果", "#2196F3", "drawable://banner_snack", 3),
            BannerEntity(4, "粮油调味", "大米 食用油 酱油 食盐", "#FF9500", "drawable://banner_grain", 4)
        )

        private fun defaultNavCategories() = listOf(
            NavCategoryEntity(1, "全部", "📦", "drawable://icon_all", CATEGORY_ALL, 1),
            NavCategoryEntity(2, "酒水", "🍺", "drawable://icon_drink", "drink", 2),
            NavCategoryEntity(3, "零食", "🍿", "drawable://icon_snack", "snack", 3),
            NavCategoryEntity(4, "粮油", "🍚", "drawable://icon_grain", "grain", 4),
            NavCategoryEntity(5, "日化", "🧴", "drawable://icon_daily", "daily", 5),
            NavCategoryEntity(6, "牛奶", "🥛", "drawable://icon_milk", "milk", 6),
            NavCategoryEntity(7, "方便", "🍜", "drawable://icon_instant", "instant", 7),
            NavCategoryEntity(8, "低温", "🧊", "drawable://icon_cold", "cold", 8)
        )

        private fun defaultProducts() = listOf(
            ProductEntity(1, "农夫山泉550ml×24瓶 整箱", "22.0", "26.0", "爆款", "💧", "drawable://img", "drink", "water", "农夫山泉天然水，550ml*24瓶/箱，民生刚需品。", 15000, "550ml×24瓶|380ml×24瓶"),
            ProductEntity(2, "雪花啤酒 勇闯天涯 500ml×12罐", "48.0", "55.0", "热销", "🍺", "drawable://img", "drink", "beer", "雪花勇闯天涯，500ml*12罐/箱，经典畅销款。", 12000, "500ml×12罐|330ml×24罐"),
            ProductEntity(3, "王老吉凉茶 310ml×24罐 整箱", "58.0", "68.0", "特惠", "🍵", "drawable://img", "drink", "tea", "王老吉凉茶植物饮料，310ml*24罐/箱。", 9800, "310ml×24罐|250ml×24盒"),
            ProductEntity(4, "康师傅冰红茶 500ml×15瓶", "36.0", "42.0", "推荐", "🧊", "drawable://img", "drink", "tea", "康师傅冰红茶，500ml*15瓶/箱，热销茶饮料。", 8500, "500ml×15瓶|330ml×24瓶"),
            ProductEntity(5, "可口可乐 330ml×24罐 整箱", "46.0", "52.0", "爆款", "🥤", "drawable://img", "drink", "cola", "可口可乐经典330ml，24罐/箱，畅销全球。", 11000, "330ml×24罐|500ml×24瓶"),
            ProductEntity(6, "雪碧 柠檬味 500ml×15瓶", "34.0", "40.0", "热销", "🍋", "drawable://img", "drink", "soda", "雪碧柠檬味汽水，500ml*15瓶/箱，清爽解渴。", 7800, "500ml×15瓶|330ml×24罐"),
            ProductEntity(7, "乐事薯片 无限系列 145g×10包", "45.0", "55.0", "爆款", "🍿", "drawable://img", "snack", "chips", "乐事无限薯片145g，10包/组，多种口味可选。", 9200, "原味|黄瓜味|番茄味"),
            ProductEntity(8, "奥利奥夹心饼干 388g×3盒", "29.9", "35.0", "热销", "🍪", "drawable://img", "snack", "biscuit", "奥利奥原味夹心饼干388g，3盒/组，经典甜点。", 8600, "原味|巧克力味|草莓味"),
            ProductEntity(9, "旺旺雪饼 520g 整箱", "32.0", "38.0", "推荐", "🍘", "drawable://img", "snack", "biscuit", "旺旺雪饼520g，膨化食品，童年回忆。", 7500, "520g袋装|84g×6袋"),
            ProductEntity(10, "徐福记沙琪玛 448g×2袋", "22.8", "28.0", "特惠", "🍬", "drawable://img", "snack", "candy", "徐福记沙琪玛448g，2袋/组，传统糕点。", 6800, "鸡蛋味|芝麻味"),
            ProductEntity(11, "洽洽瓜子 500g×3袋", "25.8", "32.0", "热销", "🌻", "drawable://img", "snack", "nuts", "洽洽香瓜子500g，3袋/组，经典炒货。", 6200, "五香味|原味|焦糖味"),
            ProductEntity(12, "三只松鼠坚果礼盒 1498g", "89.0", "108.0", "爆款", "🥜", "drawable://img", "snack", "nuts", "三只松鼠坚果礼盒1498g，年节送礼佳品。", 5400, "坚果礼盒|每日坚果"),
            ProductEntity(13, "金龙鱼调和油 5L 一级", "58.0", "68.0", "爆款", "🫒", "drawable://img", "grain", "oil", "金龙鱼调和油5L，一级压榨，煎炒烹炸通用。", 13000, "5L桶装|1.8L桶装"),
            ProductEntity(14, "福临门大米 10kg 优质粳米", "52.0", "62.0", "热销", "🍚", "drawable://img", "grain", "rice", "福临门优质粳米10kg，家庭必备主食。", 11500, "10kg袋装|5kg袋装"),
            ProductEntity(15, "海天酱油 金标生抽 1.9L", "16.8", "20.0", "推荐", "🧂", "drawable://img", "grain", "soy", "海天金标生抽1.9L，厨房调味必备。", 8900, "1.9L桶装|500ml瓶装"),
            ProductEntity(16, "中盐 精制食盐 400g×20袋", "18.0", "22.0", "包邮", "🧂", "drawable://img", "grain", "salt", "中盐精制食盐400g，20袋/箱，民生刚需。", 8200, "400g×20袋|500g×20袋"),
            ProductEntity(17, "太太乐鸡精 454g×2袋", "28.8", "35.0", "特惠", "🍗", "drawable://img", "grain", "seasoning", "太太乐鸡精454g，2袋/组，提鲜调味。", 5600, "454g×2袋|227g×4袋"),
            ProductEntity(18, "老干妈风味豆豉 280g×12瓶", "72.0", "85.0", "热销", "🌶️", "drawable://img", "grain", "sauce", "老干妈风味豆豉280g，12瓶/箱，下饭神器。", 7800, "280g×12瓶|210g×12瓶"),
            ProductEntity(19, "维达抽纸 130抽×24包 整箱", "42.0", "52.0", "爆款", "🧻", "drawable://img", "daily", "tissue", "维达抽纸130抽*24包，三层加厚，家庭必备。", 16800, "130抽×24包|150抽×24包"),
            ProductEntity(20, "立白洗洁精 1.12kg×2瓶", "18.8", "24.0", "热销", "🧴", "drawable://img", "daily", "clean", "立白洗洁精1.12kg*2瓶，强力去油。", 9200, "1.12kg×2瓶|500g×4瓶"),
            ProductEntity(21, "立白洗衣液 3kg 整箱装", "38.0", "48.0", "推荐", "🧺", "drawable://img", "daily", "laundry", "立白洗衣液3kg，低泡易漂，护色护衣。", 8500, "3kg瓶装|2kg袋装"),
            ProductEntity(22, "六神花露水 195ml×3瓶", "22.8", "28.0", "特惠", "🌸", "drawable://img", "daily", "mosquito", "六神驱蚊花露水195ml*3瓶，夏日必备。", 6700, "195ml×3瓶|95ml×6瓶"),
            ProductEntity(23, "黑人牙膏 双重薄荷 140g×4支", "36.0", "45.0", "热销", "🦷", "drawable://img", "daily", "tooth", "黑人双重薄荷牙膏140g*4支，清新口气。", 7800, "140g×4支|90g×6支"),
            ProductEntity(24, "滴露消毒液 1.2L×2瓶", "58.0", "68.0", "爆款", "🧴", "drawable://img", "daily", "disinfect", "滴露消毒液1.2L*2瓶，居家消毒必备。", 6400, "1.2L×2瓶|500ml×4瓶"),
            ProductEntity(25, "蒙牛纯牛奶 250ml×24盒 整箱", "48.0", "58.0", "爆款", "🥛", "drawable://img", "milk", "milk", "蒙牛纯牛奶250ml*24盒，常温保存，营养健康。", 14500, "250ml×24盒|200ml×24盒"),
            ProductEntity(26, "伊利纯牛奶 250ml×24盒 整箱", "48.0", "58.0", "热销", "🥛", "drawable://img", "milk", "milk", "伊利纯牛奶250ml*24盒，国民好奶。", 14200, "250ml×24盒|200ml×24盒"),
            ProductEntity(27, "特仑苏有机纯牛奶 250ml×12盒", "68.0", "78.0", "推荐", "🥛", "drawable://img", "milk", "milk", "特仑苏有机纯牛奶250ml*12盒，高端有机。", 8900, "250ml×12盒|250ml×16盒"),
            ProductEntity(28, "安慕希希腊酸奶 205g×12盒", "66.0", "76.0", "特惠", "🥛", "drawable://img", "milk", "yogurt", "安慕希希腊风味酸奶205g*12盒，浓醇好喝。", 7600, "原味|草莓味|蓝莓味"),
            ProductEntity(29, "康师傅方便面 红烧牛肉 112g×24包", "36.0", "45.0", "爆款", "🍜", "drawable://img", "instant", "noodle", "康师傅红烧牛肉面112g*24包，经典口味。", 12500, "红烧牛肉|香辣牛肉|酸菜牛肉"),
            ProductEntity(30, "统一老坛酸菜面 103g×24包", "38.0", "46.0", "热销", "🍜", "drawable://img", "instant", "noodle", "统一老坛酸菜牛肉面103g*24包，酸辣可口。", 9800, "老坛酸菜|红烧牛肉"),
            ProductEntity(31, "今麦郎方便面 1袋装 100g", "3.0", "3.5", "包邮", "🍜", "drawable://img", "instant", "noodle", "今麦郎方便面100g/袋，散装称重，灵活备货。", 6500, "红烧牛肉|香辣牛肉"),
            ProductEntity(32, "阿华田麦芽乳饮料 380g×12罐", "72.0", "85.0", "推荐", "🥤", "drawable://img", "instant", "milk", "阿华田麦芽乳饮料380g*12罐，营养美味。", 5400, "380g×12罐|250ml×24盒"),
            ProductEntity(33, "蒙牛冠益乳 100g×8杯", "28.8", "35.0", "热销", "🍮", "drawable://img", "cold", "yogurt", "蒙牛冠益乳酸奶100g*8杯，低温冷藏。", 7600, "原味|草莓味"),
            ProductEntity(34, "伊利大果粒 260g×4盒", "32.0", "38.0", "特惠", "🍓", "drawable://img", "cold", "yogurt", "伊利大果粒酸奶260g*4盒，果粒丰富。", 6800, "草莓味|黄桃味"),
            ProductEntity(35, "双汇王中王优级火腿肠 30g×9支", "16.8", "20.0", "爆款", "🌭", "drawable://img", "cold", "sausage", "双汇王中王优级火腿肠30g*9支，儿童最爱。", 11200, "30g×9支|60g×10支"),
            ProductEntity(36, "三全水饺 猪肉白菜 1kg", "18.8", "24.0", "推荐", "🥟", "drawable://img", "cold", "dumpling", "三全猪肉白菜水饺1kg，家庭必备。", 8500, "猪肉白菜|韭菜鸡蛋")
        )
    }
}