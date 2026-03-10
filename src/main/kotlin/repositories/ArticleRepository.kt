package org.delcom.repositories

import org.delcom.dao.ArticleDao
import org.delcom.dao.CategoryDao
import org.delcom.dao.UserDao
import org.delcom.entities.Article
import org.delcom.helpers.articleDaoToModel
import org.delcom.helpers.suspendTransaction
import org.delcom.tables.ArticleTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import java.util.UUID

class ArticleRepository : IArticleRepository {

    override suspend fun getAll(): List<Article> = suspendTransaction {
        ArticleDao.all().map(::articleDaoToModel)
    }

    override suspend fun getById(id: String): Article? = suspendTransaction {
        ArticleDao
            .find { ArticleTable.id eq UUID.fromString(id) }
            .limit(1)
            .map(::articleDaoToModel)
            .firstOrNull()
    }

    override suspend fun getByCategory(categoryId: String): List<Article> = suspendTransaction {
        ArticleDao
            .find { ArticleTable.categoryId eq UUID.fromString(categoryId) }
            .map(::articleDaoToModel)
    }

    override suspend fun create(article: Article): String = suspendTransaction {
        val articleDao = ArticleDao.new {
            title = article.title
            content = article.content
            thumbnail = article.thumbnail
            isPublished = article.isPublished
            category = CategoryDao.findById(UUID.fromString(article.categoryId))!!
            author = UserDao.findById(UUID.fromString(article.authorId))!!
            createdAt = article.createdAt
            updatedAt = article.updatedAt
        }
        articleDao.id.value.toString()
    }

    override suspend fun update(id: String, newArticle: Article): Boolean = suspendTransaction {
        val articleDao = ArticleDao
            .find { ArticleTable.id eq UUID.fromString(id) }
            .limit(1)
            .firstOrNull()

        if (articleDao != null) {
            articleDao.title = newArticle.title
            articleDao.content = newArticle.content
            articleDao.thumbnail = newArticle.thumbnail
            articleDao.isPublished = newArticle.isPublished
            articleDao.category = CategoryDao.findById(UUID.fromString(newArticle.categoryId))!!
            articleDao.updatedAt = newArticle.updatedAt
            true
        } else {
            false
        }
    }

    override suspend fun delete(id: String): Boolean = suspendTransaction {
        val rowsDeleted = ArticleTable.deleteWhere {
            ArticleTable.id eq UUID.fromString(id)
        }
        rowsDeleted >= 1
    }
}