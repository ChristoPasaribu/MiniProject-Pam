package org.delcom.services

import io.ktor.server.application.*
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.delcom.data.AppException
import org.delcom.data.ArticleRequest
import org.delcom.data.DataResponse
import org.delcom.helpers.JWTConstants
import org.delcom.helpers.ValidatorHelper
import org.delcom.repositories.IArticleRepository

class ArticleService(private val articleRepository: IArticleRepository) {

    suspend fun getAll(call: ApplicationCall) {
        val articles = articleRepository.getAll()
        val response = DataResponse(
            "success",
            "Berhasil mengambil data artikel",
            articles
        )
        call.respond(response)
    }

    suspend fun getById(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "Id tidak boleh kosong")

        val article = articleRepository.getById(id)
            ?: throw AppException(404, "Artikel tidak ditemukan")

        val response = DataResponse(
            "success",
            "Berhasil mengambil data artikel",
            article
        )
        call.respond(response)
    }

    suspend fun getByCategory(call: ApplicationCall) {
        val categoryId = call.parameters["categoryId"]
            ?: throw AppException(400, "Category Id tidak boleh kosong")

        val articles = articleRepository.getByCategory(categoryId)
        val response = DataResponse(
            "success",
            "Berhasil mengambil data artikel",
            articles
        )
        call.respond(response)
    }

    suspend fun create(call: ApplicationCall) {
        val request = call.receive<ArticleRequest>()

        val validator = ValidatorHelper(request.toMap())
        validator.required("title", "Judul tidak boleh kosong")
        validator.required("content", "Konten tidak boleh kosong")
        validator.required("categoryId", "Kategori tidak boleh kosong")
        validator.validate()

        // ambil userId dari JWT
        val authorId = call.principal<io.ktor.server.auth.jwt.JWTPrincipal>()
            ?.payload?.getClaim("userId")?.asString()
            ?: throw AppException(401, "Token tidak valid")

        request.authorId = authorId
        val id = articleRepository.create(request.toEntity())

        val response = DataResponse(
            "success",
            "Berhasil membuat artikel",
            mapOf(Pair("articleId", id))
        )
        call.respond(response)
    }

    suspend fun update(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "Id tidak boleh kosong")

        articleRepository.getById(id)
            ?: throw AppException(404, "Artikel tidak ditemukan")

        val request = call.receive<ArticleRequest>()

        val validator = ValidatorHelper(request.toMap())
        validator.required("title", "Judul tidak boleh kosong")
        validator.required("content", "Konten tidak boleh kosong")
        validator.required("categoryId", "Kategori tidak boleh kosong")
        validator.validate()

        val updated = articleRepository.update(id, request.toEntity())
        if (!updated) throw AppException(500, "Gagal mengupdate artikel")

        val response = DataResponse(
            "success",
            "Berhasil mengupdate artikel",
            null
        )
        call.respond(response)
    }

    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "Id tidak boleh kosong")

        articleRepository.getById(id)
            ?: throw AppException(404, "Artikel tidak ditemukan")

        val deleted = articleRepository.delete(id)
        if (!deleted) throw AppException(500, "Gagal menghapus artikel")

        val response = DataResponse(
            "success",
            "Berhasil menghapus artikel",
            null
        )
        call.respond(response)
    }
}