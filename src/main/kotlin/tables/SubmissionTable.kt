package org.delcom.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object SubmissionTable : UUIDTable("submissions") {
    val userId = reference("user_id", UserTable)
    val challengeId = reference("challenge_id", ChallengeTable)
    val flag = varchar("flag", 255)             // flag yang di-submit user
    val isCorrect = bool("is_correct")
    val pointsEarned = integer("points_earned").default(0)  // poin setelah dipotong hint
    val submittedAt = timestamp("submitted_at")
}