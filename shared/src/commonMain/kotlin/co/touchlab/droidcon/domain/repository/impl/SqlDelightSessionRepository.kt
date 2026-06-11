package co.touchlab.droidcon.domain.repository.impl

import co.touchlab.droidcon.domain.entity.Session
import co.touchlab.droidcon.domain.repository.SessionRepository
import co.touchlab.droidcon.domain.repository.db.queries.SessionQueries
import co.touchlab.droidcon.domain.repository.db.table.SessionTable
import co.touchlab.droidcon.domain.service.DateTimeService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class SqlDelightSessionRepository(private val dateTimeService: DateTimeService, private val sessionQueries: SessionQueries) :
    BaseRepository<Session.Id, Session>(),
    SessionRepository {

    init {
        SessionTable.dateTimeService = dateTimeService
    }

    override fun observe(id: Session.Id, conferenceId: Long): Flow<Session> =
        flow { emit(sessionQueries.sessionById(id.value, conferenceId)!!) }

    fun sessionById(id: Session.Id, conferenceId: Long): Session? =
        sessionQueries.sessionById(id.value, conferenceId)

    override fun observeOrNull(id: Session.Id, conferenceId: Long): Flow<Session?> =
        flow { emit(sessionQueries.sessionById(id.value, conferenceId)) }

    override fun observeAllAttending(conferenceId: Long): Flow<List<Session>> =
        flow { emit(sessionQueries.attendingSessions(conferenceId)) }

    override suspend fun allAttending(conferenceId: Long): List<Session> = observeAllAttending(conferenceId).first()

    override suspend fun setRsvp(sessionId: Session.Id, rsvp: Session.RSVP, conferenceId: Long) {
        sessionQueries.updateRsvp(rsvp.isAttending, sessionId.value, conferenceId)
    }

    override suspend fun setRsvpSent(sessionId: Session.Id, isSent: Boolean, conferenceId: Long) {
        sessionQueries.updateRsvpSent(isSent, sessionId.value, conferenceId)
    }

    override suspend fun setFeedback(sessionId: Session.Id, feedback: Session.Feedback, conferenceId: Long) {
        sessionQueries.updateFeedBack(feedback.rating, feedback.comment, sessionId.value, conferenceId)
    }

    override suspend fun setFeedbackSent(sessionId: Session.Id, isSent: Boolean, conferenceId: Long) {
        sessionQueries.updateFeedBackSent(isSent, sessionId.value, conferenceId)
    }

    override fun allSync(conferenceId: Long): List<Session> = sessionQueries.allSessions(conferenceId)

    override fun findSync(id: Session.Id, conferenceId: Long): Session? =
        sessionQueries.sessionById(id.value, conferenceId)

    override fun observeAll(conferenceId: Long): Flow<List<Session>> =
        flow { emit(sessionQueries.allSessions(conferenceId)) }

    override fun doUpsert(entity: Session, conferenceId: Long) {
        sessionQueries.upsert(entity,conferenceId)
    }

    override fun doDelete(id: Session.Id, conferenceId: Long) {
        sessionQueries.deleteById(id.value, conferenceId)
    }

    override fun contains(id: Session.Id, conferenceId: Long): Boolean =
        sessionQueries.existsById(id.value, conferenceId)
}
