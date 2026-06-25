package co.touchlab.droidcon.domain.repository.impl

import co.touchlab.droidcon.domain.entity.Conference
import co.touchlab.droidcon.domain.repository.ConferenceRepository
import co.touchlab.droidcon.domain.repository.db.queries.ConferenceQueries
import co.touchlab.droidcon.domain.repository.db.table.ConferenceTable
import co.touchlab.kermit.Logger
import com.kqlite.cursor.asCallbackFlow
import com.kqlite.cursor.mapToList
import com.kqlite.cursor.mapToSingle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class SqlDelightConferenceRepository(
    private val conferenceQueries: ConferenceQueries,
    private val log: Logger = Logger.withTag("SqlDelightConferenceRepository"),
) : ConferenceRepository {

    override fun observeAll(): Flow<List<Conference>> =
        conferenceQueries.selectAllActive().asCallbackFlow().mapToList(Dispatchers.Main, ConferenceTable::mapper)

    override fun observeSelected(): Flow<Conference> =
        conferenceQueries.selectSelected().asCallbackFlow().mapToSingle(Dispatchers.Main, ConferenceTable::mapper)

    override suspend fun getSelected(): Conference = conferenceQueries.selectSelected().mapToSingle(ConferenceTable::mapper)

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
        return conferenceQueries.insert(conference)
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
