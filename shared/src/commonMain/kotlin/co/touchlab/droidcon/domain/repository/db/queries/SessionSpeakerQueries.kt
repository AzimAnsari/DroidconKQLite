package co.touchlab.droidcon.domain.repository.db.queries

import co.touchlab.droidcon.domain.repository.db.table.SessionSpeakerTable
import com.kqlite.statement.quickDelete
import com.kqlite.statement.quickInsert
import com.kqlite.table.Action

class SessionSpeakerQueries {
    fun deleteBySessionId(value: String, conferenceId: Long) {
        SessionSpeakerTable.quickDelete {
            (it.sessionId EQ value).AND(it.conferenceId EQ conferenceId)
        }
    }

    fun insertUpdate(sessionId: String, speakerId: String, conferenceId: Long, displayOrder: Long) {
        /*
        SessionSpeakerTable
            .insert(
                SessionSpeakerTable.sessionId,
                SessionSpeakerTable.speakerId,
                SessionSpeakerTable.conferenceId,
                SessionSpeakerTable.displayOrder,
                onConflict = Action.REPLACE,
            ).bind {
                it.sessionId.bind(sessionId)
                it.speakerId.bind(speakerId)
                it.conferenceId.bind(conferenceId)
                it.displayOrder.bind(displayOrder)
            }.execute()
        */
        SessionSpeakerTable.quickInsert(onConflict = Action.REPLACE) {
            it.sessionId.bind(sessionId)
            it.speakerId.bind(speakerId)
            it.conferenceId.bind(conferenceId)
            it.displayOrder.bind(displayOrder)
        }
    }
}
