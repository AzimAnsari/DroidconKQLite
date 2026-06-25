package co.touchlab.droidcon.domain.repository.impl

import co.touchlab.droidcon.domain.entity.Profile
import co.touchlab.droidcon.domain.entity.Session
import co.touchlab.droidcon.domain.entity.Sponsor
import co.touchlab.droidcon.domain.repository.ProfileRepository
import co.touchlab.droidcon.domain.repository.db.queries.ProfileQueries
import co.touchlab.droidcon.domain.repository.db.queries.SessionSpeakerQueries
import co.touchlab.droidcon.domain.repository.db.queries.SponsorRepresentativeQueries
import co.touchlab.droidcon.domain.repository.db.table.ProfileTable
import com.kqlite.cursor.asCallbackFlow
import com.kqlite.cursor.mapToList
import com.kqlite.cursor.mapToSingle
import com.kqlite.cursor.mapToSingleOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class SqlDelightProfileRepository(
    private val profileQueries: ProfileQueries,
    private val speakerQueries: SessionSpeakerQueries,
    private val representativeQueries: SponsorRepresentativeQueries,
) : BaseRepository<Profile.Id, Profile>(),
    ProfileRepository {

    override suspend fun getSpeakersBySession(id: Session.Id, conferenceId: Long): List<Profile> =
        profileQueries.selectBySession(id.value, conferenceId)

    override fun setSessionSpeakers(session: Session, speakers: List<Profile.Id>, conferenceId: Long) {
        speakerQueries.deleteBySessionId(session.id.value, conferenceId)
        speakers.forEachIndexed { index, speakerId ->
            speakerQueries.insertUpdate(
                sessionId = session.id.value,
                speakerId = speakerId.value,
                conferenceId = conferenceId,
                displayOrder = index.toLong(),
            )
        }
    }

    override fun setSponsorRepresentatives(sponsor: Sponsor, representatives: List<Profile.Id>, conferenceId: Long) {
        representativeQueries.deleteBySponsorId(
            sponsorName = sponsor.id.name,
            sponsorGroupName = sponsor.id.group,
            conferenceId = conferenceId,
        )
        representatives.forEachIndexed { index, representativeId ->
            representativeQueries.insertUpdate(
                sponsorName = sponsor.name,
                sponsorGroupName = sponsor.id.group,
                representativeId = representativeId.value,
                conferenceId = conferenceId,
                displayOrder = index.toLong(),
            )
        }
    }

    override suspend fun getSponsorRepresentatives(sponsorId: Sponsor.Id, conferenceId: Long): List<Profile> =
        profileQueries.selectBySponsor(
            sponsorName = sponsorId.name,
            sponsorGroupName = sponsorId.group,
            conferenceId = conferenceId,
        )

    override fun allSync(conferenceId: Long): List<Profile> =
        profileQueries.selectAll(conferenceId).mapToList(ProfileTable::mapper)

    override fun observe(id: Profile.Id, conferenceId: Long): Flow<Profile> =
        profileQueries.selectById(id.value, conferenceId).asCallbackFlow().mapToSingle(Dispatchers.Main, ProfileTable::mapper)

    override fun observeOrNull(id: Profile.Id, conferenceId: Long): Flow<Profile?> =
        profileQueries.selectById(id.value, conferenceId).asCallbackFlow().mapToSingleOrNull(Dispatchers.Main, ProfileTable::mapper)

    override fun observeAll(conferenceId: Long): Flow<List<Profile>> =
        profileQueries.selectAll(conferenceId).asCallbackFlow().mapToList(Dispatchers.Main, ProfileTable::mapper)

    override fun doUpsert(entity: Profile, conferenceId: Long) {
        profileQueries.upsert(entity, conferenceId)
    }

    override fun doDelete(id: Profile.Id, conferenceId: Long) {
        profileQueries.delete(id.value, conferenceId)
    }

    override fun contains(id: Profile.Id, conferenceId: Long): Boolean =
        profileQueries.existsById(id.value, conferenceId)
}
