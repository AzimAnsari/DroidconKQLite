package co.touchlab.droidcon.domain.repository.db.queries

import co.touchlab.droidcon.domain.entity.Session
import co.touchlab.droidcon.domain.repository.db.table.SessionSpeakerTable
import co.touchlab.droidcon.domain.repository.db.table.SessionTable
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.functions.COUNT
import com.kqlite.operator.Sort
import com.kqlite.statement.quickDelete
import com.kqlite.statement.quickInsert
import com.kqlite.statement.select
import com.kqlite.statement.update
import com.kqlite.table.Action

class SessionQueries {

    private fun mapToSession(cursor: KQLiteCursor): List<Session> =
        cursor.use {
            it.asSequence().map(SessionTable::mapSession).toList()
        }

    fun sessionById(id: String, conferenceId: Long): Session? {
        val cursor = SessionTable
            .select()
            .where {
                (it.id EQ id) AND (it.conferenceId EQ conferenceId)
            }.execute()

        return mapToSession(cursor).firstOrNull()
    }

    fun attendingSessions(conferenceId: Long): List<Session> {
        val cursor = SessionTable
            .select()
            .where {
                (it.rsvp NOT_EQ false) AND (it.conferenceId EQ conferenceId)
            }.orderBy(SessionTable.startsAt, Sort.ASC)
            .execute()

        return mapToSession(cursor).toList()
    }

    fun updateRsvp(attending: Boolean, sessionId: String, conferenceId: Long) {
        SessionTable
            .update {
                it.rsvp.bind(attending)
            }.where {
                (it.id EQ sessionId) AND (it.conferenceId EQ conferenceId)
            }.execute()
    }

    fun updateRsvpSent(sent: Boolean, sessionId: String, conferenceId: Long) {
        SessionTable
            .update {
                it.rsvpSent.bind(sent)
            }.where {
                (it.id EQ sessionId) AND (it.conferenceId EQ conferenceId)
            }.execute()
    }

    fun updateFeedBack(rating: Int, comment: String, sessionId: String, conferenceId: Long) {
        SessionTable
            .update {
                it.feedbackRating.bind(rating)
                it.feedbackComment.bind(comment)
                it.feedbackSent.bind(false)
            }.where {
                (it.id EQ sessionId) AND (it.conferenceId EQ conferenceId)
            }.execute()
    }

    fun updateFeedBackSent(sent: Boolean, sessionId: String, conferenceId: Long) {
        SessionTable
            .update {
                it.feedbackSent.bind(sent)
            }.where {
                (it.id EQ sessionId) AND (it.conferenceId EQ conferenceId)
            }.execute()
    }

    fun allSessions(conferenceId: Long): List<Session> {
        val cursor = SessionTable
            .select()
            .where {
                (it.conferenceId EQ conferenceId)
            }.orderBy(SessionTable.startsAt, Sort.ASC)
            .execute()

        return mapToSession(cursor).toList()
    }

    fun upsert(entity: Session, conferenceId: Long) {
        SessionTable.quickInsert(onConflict = Action.REPLACE) {
            it.bindSession(this, entity, conferenceId)
        }
    }

    fun deleteById(sessionId: String, conferenceId: Long) {
        SessionSpeakerTable.quickDelete { it.sessionId.EQ(sessionId) }
        SessionTable.quickDelete { it.id.EQ(sessionId) AND it.conferenceId.EQ(conferenceId) }
    }

    fun existsById(sessionId: String, conferenceId: Long): Boolean {
        return SessionTable
            .select(COUNT())
            .where {
                (it.id EQ sessionId) AND (it.conferenceId EQ conferenceId)
            }.execute()
            .use {
                it.hasNext()
            }
    }
}
