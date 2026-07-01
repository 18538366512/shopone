package com.xby.shop624.data.repository

import com.xby.shop624.data.local.dao.CommentDao
import com.xby.shop624.data.local.dao.ProductDao
import com.xby.shop624.data.local.entity.CommentEntity
import com.xby.shop624.data.local.entity.ProductEntity
import com.xby.shop624.model.CommentItem

class ProductRepository(
    private val productDao: ProductDao,
    private val commentDao: CommentDao
) {

    suspend fun getProduct(id: Int): ProductEntity? = productDao.getById(id)

    suspend fun getCommentItems(productId: Int): List<CommentItem> {
        val all = commentDao.getByProduct(productId)
        val topLevel = all.filter { it.parentId == null }
        return topLevel.map { comment ->
            CommentItem(
                comment = comment,
                replies = all.filter { it.parentId == comment.id }
            )
        }
    }

    suspend fun addComment(
        productId: Int,
        userName: String,
        content: String,
        parentId: Long? = null
    ): Long {
        return commentDao.insert(
            CommentEntity(
                productId = productId,
                userName = userName,
                content = content,
                createTime = System.currentTimeMillis(),
                parentId = parentId
            )
        )
    }

    suspend fun ensureDefaultComments() {
        if (commentDao.getCount() > 0) return
        val now = System.currentTimeMillis()
        val c1 = commentDao.insert(
            CommentEntity(
                productId = 1,
                userName = "小王",
                content = "苹果很新鲜，个头也大，回购好几次了！",
                createTime = now - 86400000 * 2
            )
        )
        commentDao.insert(
            CommentEntity(
                productId = 1,
                userName = "帮便利客服",
                content = "感谢支持，我们会继续为您提供优质商品~",
                createTime = now - 86400000 * 2 + 3600000,
                parentId = c1
            )
        )
        commentDao.insert(
            CommentEntity(
                productId = 1,
                userName = "李姐",
                content = "配送很快，包装也很好。",
                createTime = now - 86400000
            )
        )
        val c3 = commentDao.insert(
            CommentEntity(
                productId = 4,
                userName = "张三",
                content = "鸡蛋很新鲜，没有破损。",
                createTime = now - 86400000 * 3
            )
        )
        commentDao.insert(
            CommentEntity(
                productId = 4,
                userName = "帮便利客服",
                content = "谢谢好评，破损包赔哦！",
                createTime = now - 86400000 * 3 + 7200000,
                parentId = c3
            )
        )
        commentDao.insert(
            CommentEntity(
                productId = 6,
                userName = "社区团长",
                content = "大米口感不错，邻居们都说好。",
                createTime = now - 86400000 * 5
            )
        )
    }
}
