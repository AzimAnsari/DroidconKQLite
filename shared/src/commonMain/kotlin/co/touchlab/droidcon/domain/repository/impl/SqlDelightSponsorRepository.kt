package co.touchlab.droidcon.domain.repository.impl

import co.touchlab.droidcon.domain.entity.Sponsor
import co.touchlab.droidcon.domain.repository.SponsorRepository
import co.touchlab.droidcon.domain.repository.db.queries.SponsorQueries
import co.touchlab.droidcon.domain.repository.db.table.SponsorTable
import com.kqlite.cursor.asCallbackFlow
import com.kqlite.cursor.mapToList
import com.kqlite.cursor.mapToSingle
import com.kqlite.cursor.mapToSingleOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class SqlDelightSponsorRepository(private val sponsorQueries: SponsorQueries) :
    BaseRepository<Sponsor.Id, Sponsor>(),
    SponsorRepository {

    override fun observe(id: Sponsor.Id, conferenceId: Long): Flow<Sponsor> =
        sponsorQueries.sponsorById(id.name, id.group, conferenceId)
            .asCallbackFlow().mapToSingle(Dispatchers.Main, SponsorTable::mapper)

    override fun observeOrNull(id: Sponsor.Id, conferenceId: Long): Flow<Sponsor?> =
        sponsorQueries.sponsorById(id.name, id.group, conferenceId)
            .asCallbackFlow().mapToSingleOrNull(Dispatchers.Main, SponsorTable::mapper)

    override fun observeAll(conferenceId: Long): Flow<List<Sponsor>> =
        sponsorQueries.selectAll(conferenceId).asCallbackFlow().mapToList(Dispatchers.Main, SponsorTable::mapper)

    override fun contains(id: Sponsor.Id, conferenceId: Long): Boolean =
        sponsorQueries.existsById(id.name, id.group, conferenceId)

    override suspend fun allByGroupName(group: String, conferenceId: Long): List<Sponsor> =
        sponsorQueries.sponsorsByGroup(group, conferenceId).mapToList(SponsorTable::mapper)

    override fun allSync(conferenceId: Long): List<Sponsor> = sponsorQueries.selectAll(conferenceId).mapToList(SponsorTable::mapper)

    override fun doUpsert(entity: Sponsor, conferenceId: Long) {
        sponsorQueries.upsert(entity, conferenceId)
    }

    override fun doDelete(id: Sponsor.Id, conferenceId: Long) {
        sponsorQueries.deleteById(id.name, id.group, conferenceId)
    }
}
