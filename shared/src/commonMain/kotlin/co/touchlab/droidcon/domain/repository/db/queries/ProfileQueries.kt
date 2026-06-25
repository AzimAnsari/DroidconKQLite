package co.touchlab.droidcon.domain.repository.db.queries

import co.touchlab.droidcon.domain.entity.Profile
import co.touchlab.droidcon.domain.repository.db.table.ProfileTable
import co.touchlab.droidcon.domain.repository.db.table.SessionSpeakerTable
import co.touchlab.droidcon.domain.repository.db.table.SponsorRepresentativeTable
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.cursor.mapToList
import com.kqlite.functions.COUNT
import com.kqlite.statement.insert
import com.kqlite.statement.quickDelete
import com.kqlite.statement.quickSelect
import com.kqlite.statement.select
import com.kqlite.table.Action

class ProfileQueries {
    fun selectBySession(value: String, conferenceId: Long): List<Profile> {
        val cursor = ProfileTable
            .select()
            .innerJoin(SessionSpeakerTable)
            .on(
                SessionSpeakerTable.speakerId, ProfileTable.id,
                and = {
                    SessionSpeakerTable.conferenceId EQ ProfileTable.conferenceId
                },
            )
            .where {
                (SessionSpeakerTable.sessionId EQ value).AND(it.conferenceId EQ conferenceId)
            }
            .orderBy(SessionSpeakerTable.displayOrder)
            .execute()

        return cursor.mapToList(ProfileTable::mapper)
    }

    fun selectBySponsor(sponsorName: String, sponsorGroupName: String, conferenceId: Long): List<Profile> {
        val cursor = ProfileTable
            .select()
            .innerJoin(SponsorRepresentativeTable)
            .on(
                SponsorRepresentativeTable.representativeId, ProfileTable.id,
                and = {
                    SponsorRepresentativeTable.conferenceId EQ ProfileTable.conferenceId
                },
            )
            .where {
                (SponsorRepresentativeTable.sponsorName EQ sponsorName) AND
                    (SponsorRepresentativeTable.sponsorGroupName EQ sponsorGroupName) AND
                    (it.conferenceId EQ conferenceId)
            }.orderBy(SponsorRepresentativeTable.displayOrder)
            .execute()

        return cursor.mapToList(ProfileTable::mapper)
    }

    fun selectAll(conferenceId: Long): KQLiteCursor {
        return ProfileTable.quickSelect(
            where = { it.conferenceId EQ conferenceId },
        )
    }

    fun selectById(value: String, conferenceId: Long): KQLiteCursor {
        val cursor = ProfileTable.quickSelect(
            where = {
                (it.id EQ value).AND(it.conferenceId EQ conferenceId)
            },
        )
        return cursor
    }

    fun upsert(entity: Profile, conferenceId: Long) {
        ProfileTable.insert(onConflict = Action.REPLACE)
            .bind {
                it.conferenceId.bind(conferenceId)
                it.binder(this, entity)
            }.execute()
    }

    fun delete(value: String, conferenceId: Long) {
        SessionSpeakerTable.quickDelete {
            it.speakerId EQ value
        }

        ProfileTable.quickDelete {
            it.id.EQ(value) AND
                it.conferenceId.EQ(conferenceId)
        }
    }

    fun existsById(value: String, conferenceId: Long): Boolean {
        val cursor = select(COUNT())
            .from(ProfileTable)
            .where {
                (it.id EQ value) AND (it.conferenceId EQ conferenceId)
            }.execute()

        return cursor.use { it.getInt(0) > 0 }
    }
}
