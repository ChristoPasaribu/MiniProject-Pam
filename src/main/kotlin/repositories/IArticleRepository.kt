package org.delcom.repositories

import org.delcom.entities.Article

interface IArticleRepository {
    suspend fun getAll(): List<Article>
    suspend fun getById(id: String): Article?
    suspend fun getByCategory(categoryId: String): List<Article>
    suspend fun create(article: Article): String
    suspend fun update(id: String, newArticle: Article): Boolean
    suspend fun delete(id: String): Boolean
}