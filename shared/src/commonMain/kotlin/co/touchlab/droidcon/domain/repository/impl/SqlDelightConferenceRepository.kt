package co.touchlab.droidcon.domain.repository.impl

import co.touchlab.droidcon.domain.entity.Conference
import co.touchlab.droidcon.domain.repository.ConferenceRepository
import co.touchlab.droidcon.domain.repository.db.queries.ConferenceQueries
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.Flow

class SqlDelightConferenceRepository(
    private val conferenceQueries: ConferenceQueries,
    private val log: Logger = Logger.withTag("SqlDelightConferenceRepository"),
) : ConferenceRepository {

    override fun observeAll(): Flow<List<Conference>> =
        conferenceQueries.observeAllActive()

    override fun observeSelected(): Flow<Conference> =
        conferenceQueries.observeSelected()

    override suspend fun getSelected(): Conference = conferenceQueries.selectSelected()

    override suspend fun select(conferenceId: Long): Boolean {
        try {
            conferenceQueries.changeSelectedConference(conferenceId)
            return true
        } catch (e: Exception) {
            log.e(e) { "Error selecting conference" }
            return false
        }
    }

    override suspend fun add(conference: Conference): Long {
        return conferenceQueries.insertConference(conference)
    }

    override suspend fun update(conference: Conference): Boolean {
        try {
            return conferenceQueries.updateConference(conference)
        } catch (e: Exception) {
            log.e(e) { "Error updating conference" }
            return false
        }
    }

    override suspend fun delete(conferenceId: Long): Boolean {
        return conferenceQueries.deleteById(conferenceId)
    }
}
