package co.touchlab.droidcon.domain.repository.impl

import co.touchlab.droidcon.domain.entity.Sponsor
import co.touchlab.droidcon.domain.repository.SponsorRepository
import co.touchlab.droidcon.domain.repository.db.queries.SponsorQueries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SqlDelightSponsorRepository(private val sponsorQueries: SponsorQueries) :
    BaseRepository<Sponsor.Id, Sponsor>(),
    SponsorRepository {

    override fun observe(id: Sponsor.Id, conferenceId: Long): Flow<Sponsor> =
        flow { emit(sponsorQueries.sponsorById(id.name, id.group, conferenceId)!!) }

    override fun observeOrNull(id: Sponsor.Id, conferenceId: Long): Flow<Sponsor?> =
        flow { emit(sponsorQueries.sponsorById(id.name, id.group, conferenceId)) }

    override fun observeAll(conferenceId: Long): Flow<List<Sponsor>> =
        flow { emit(sponsorQueries.selectAll(conferenceId)) }

    override fun contains(id: Sponsor.Id, conferenceId: Long): Boolean =
        sponsorQueries.existsById(id.name, id.group, conferenceId)

    override suspend fun allByGroupName(group: String, conferenceId: Long): List<Sponsor> =
        sponsorQueries.sponsorsByGroup(group, conferenceId)

    override fun allSync(conferenceId: Long): List<Sponsor> = sponsorQueries.selectAll(conferenceId)

    override fun doUpsert(entity: Sponsor, conferenceId: Long) {
        sponsorQueries.upsert(entity, conferenceId)
    }

    override fun doDelete(id: Sponsor.Id, conferenceId: Long) {
        sponsorQueries.deleteById(id.name, id.group, conferenceId)
    }
}
