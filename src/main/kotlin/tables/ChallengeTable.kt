package org.delcom.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object ChallengeTable : UUIDTable("challenges") {
    val title = varchar("title", 200)
    val description = text("description")
    val flag = varchar("flag", 255)         // disimpan as bcrypt hash
    val points = integer("points")
    val difficulty = enumerationByName("difficulty", 10, ChallengeDifficulty::class)
    val isActive = bool("is_active").default(true)
    val createdBy = reference("created_by", UserTable)  // admin yg buat
    val categoryId = reference("category_id", CategoryTable)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}

enum class ChallengeDifficulty {
    EASY,
    MEDIUM,
    HARD
}