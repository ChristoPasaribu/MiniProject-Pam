package org.delcom.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object CategoryTable : UUIDTable("categories") {
    val name = varchar("name", 100)         // Web, Crypto, Pwn, Forensic, Misc
    val description = text("description").nullable()
    val icon = varchar("icon", 100).nullable()  // icon name / url
    val createdAt = timestamp("created_at")
}