package co.touchlab.droidcon.domain.repository.db.queries

import co.touchlab.droidcon.domain.entity.Profile
import co.touchlab.droidcon.domain.repository.db.table.ProfileTable
import co.touchlab.droidcon.domain.repository.db.table.SessionSpeakerTable
import co.touchlab.droidcon.domain.repository.db.table.SponsorRepresentativeTable
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.functions.COUNT
import com.kqlite.statement.insert
import com.kqlite.statement.quickDelete
import com.kqlite.statement.quickSelect
import com.kqlite.statement.select
import com.kqlite.table.Action

class ProfileQueries {
    private fun mapToList(cursor: KQLiteCursor): List<Profile> =
        cursor.use {
            it.asSequence().map(ProfileTable::mapProfile).toList()
        }

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

        return mapToList(cursor)
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

        return mapToList(cursor)
    }

    fun selectAll(conferenceId: Long): List<Profile> {
        val cursor = ProfileTable.quickSelect(
            where = { it.conferenceId EQ conferenceId },
        )

        return mapToList(cursor)
    }

    fun selectById(value: String, conferenceId: Long): Profile? {
        val cursor = ProfileTable.quickSelect(
            where = {
                (it.id EQ value).AND(it.conferenceId EQ conferenceId)
            },
        )
        return mapToList(cursor).firstOrNull()
    }

    fun upsert(entity: Profile, conferenceId: Long) {
        ProfileTable.insert(onConflict = Action.REPLACE)
            .bind {
                it.bindProfile(this, entity, conferenceId)
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
