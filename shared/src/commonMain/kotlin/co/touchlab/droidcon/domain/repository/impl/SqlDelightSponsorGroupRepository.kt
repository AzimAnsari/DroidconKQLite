package co.touchlab.droidcon.domain.repository.impl

import co.touchlab.droidcon.domain.entity.SponsorGroup
import co.touchlab.droidcon.domain.repository.SponsorGroupRepository
import co.touchlab.droidcon.domain.repository.db.queries.SponsorGroupQueries
import co.touchlab.droidcon.domain.repository.db.table.SponsorGroupTable
import com.kqlite.cursor.asCallbackFlow
import com.kqlite.cursor.mapToList
import com.kqlite.cursor.mapToSingle
import com.kqlite.cursor.mapToSingleOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class SqlDelightSponsorGroupRepository(private val sponsorGroupQueries: SponsorGroupQueries) :
    BaseRepository<SponsorGroup.Id, SponsorGroup>(),
    SponsorGroupRepository {

    override fun allSync(conferenceId: Long): List<SponsorGroup> =
        sponsorGroupQueries.selectAll(conferenceId).mapToList(SponsorGroupTable::mapper)

    override fun observe(id: SponsorGroup.Id, conferenceId: Long): Flow<SponsorGroup> =
        sponsorGroupQueries.sponsorGroupByName(id.value, conferenceId)
            .asCallbackFlow().mapToSingle(Dispatchers.Main, SponsorGroupTable::mapper)

    override fun observeOrNull(id: SponsorGroup.Id, conferenceId: Long): Flow<SponsorGroup?> =
        sponsorGroupQueries.sponsorGroupByName(id.value, conferenceId)
            .asCallbackFlow().mapToSingleOrNull(Dispatchers.Main, SponsorGroupTable::mapper)

    override fun observeAll(conferenceId: Long): Flow<List<SponsorGroup>> =
        sponsorGroupQueries.selectAll(conferenceId)
            .asCallbackFlow().mapToList(Dispatchers.Main, SponsorGroupTable::mapper)

    override fun contains(id: SponsorGroup.Id, conferenceId: Long): Boolean =
        sponsorGroupQueries.existsByName(id.value, conferenceId)

    override fun doUpsert(entity: SponsorGroup, conferenceId: Long) {
        sponsorGroupQueries.upsert(entity, conferenceId)
    }

    override fun doDelete(id: SponsorGroup.Id, conferenceId: Long) {
        sponsorGroupQueries.deleteByName(id.value, conferenceId)
    }
}
