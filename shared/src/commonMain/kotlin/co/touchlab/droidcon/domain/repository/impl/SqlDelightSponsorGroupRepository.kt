package co.touchlab.droidcon.domain.repository.impl

import co.touchlab.droidcon.domain.entity.SponsorGroup
import co.touchlab.droidcon.domain.repository.SponsorGroupRepository
import co.touchlab.droidcon.domain.repository.db.queries.SponsorGroupQueries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SqlDelightSponsorGroupRepository(private val sponsorGroupQueries: SponsorGroupQueries) :
    BaseRepository<SponsorGroup.Id, SponsorGroup>(),
    SponsorGroupRepository {

    override fun allSync(conferenceId: Long): List<SponsorGroup> =
        sponsorGroupQueries.selectAll(conferenceId)

    override fun observe(id: SponsorGroup.Id, conferenceId: Long): Flow<SponsorGroup> =
        flow { emit(sponsorGroupQueries.sponsorGroupByName(id.value, conferenceId)!!) }

    override fun observeOrNull(id: SponsorGroup.Id, conferenceId: Long): Flow<SponsorGroup?> =
        flow { emit(sponsorGroupQueries.sponsorGroupByName(id.value, conferenceId)) }

    override fun observeAll(conferenceId: Long): Flow<List<SponsorGroup>> =
        flow { emit(sponsorGroupQueries.selectAll(conferenceId)) }

    override fun contains(id: SponsorGroup.Id, conferenceId: Long): Boolean =
        sponsorGroupQueries.existsByName(id.value, conferenceId)

    override fun doUpsert(entity: SponsorGroup, conferenceId: Long) {
        sponsorGroupQueries.upsert(entity, conferenceId)
    }

    override fun doDelete(id: SponsorGroup.Id, conferenceId: Long) {
        sponsorGroupQueries.deleteByName(id.value, conferenceId)
    }
}
