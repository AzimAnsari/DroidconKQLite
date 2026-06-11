package co.touchlab.droidcon.domain.repository.db.table

import com.kqlite.column.notNull
import com.kqlite.constraint.ConstraintBuilder
import com.kqlite.table.KQLiteTable

object SessionSpeakerTable: KQLiteTable("sessionSpeakerTable") {
    val sessionId = textColumn("sessionId").notNull()
    val speakerId = textColumn("speakerId").notNull()
    val conferenceId = integerColumn("conferenceId").notNull()
    val displayOrder = integerColumn("displayOrder").notNull().default(0)

    override val constraints: (ConstraintBuilder.() -> Unit)
        get() = {
            primaryKey(sessionId, speakerId, conferenceId)
            foreignKey(sessionId, conferenceId).references(SessionTable.id, SessionTable.conferenceId)
            foreignKey(speakerId, conferenceId).references(ProfileTable.id, ProfileTable.conferenceId)
            foreignKey(conferenceId).references(ConferenceTable.id)
        }
}
