package org.delcom.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object HintTable : UUIDTable("hints") {
    val challengeId = reference("challenge_id", ChallengeTable)
    val content = text("content")
    val cost = integer("cost").default(0)   // penalty poin kalau buka hint
    val orderIndex = integer("order_index") // urutan hint (hint 1, hint 2, dst)
    val createdAt = timestamp("created_at")
}