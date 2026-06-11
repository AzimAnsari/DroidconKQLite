package co.touchlab.droidcon.domain.repository.db.queries

import co.touchlab.droidcon.domain.entity.Sponsor
import co.touchlab.droidcon.domain.repository.db.table.SponsorTable
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.functions.COUNT
import com.kqlite.statement.delete
import com.kqlite.statement.insert
import com.kqlite.statement.select
import com.kqlite.table.Action

class SponsorQueries {

    fun mapToList(cursor: KQLiteCursor): List<Sponsor> =
        cursor.use {
            it.asSequence().map(SponsorTable::mapSponsor).toList()
        }

    fun sponsorById(name: String, group: String, conferenceId: Long): Sponsor? {
        val cursor = SponsorTable
            .select()
            .where {
                (SponsorTable.name EQ name) AND (SponsorTable.groupName EQ group) AND (SponsorTable.conferenceId EQ conferenceId)
            }.limit(1)
            .execute()

        return mapToList(cursor).firstOrNull()
    }

    fun selectAll(conferenceId: Long): List<Sponsor> {
        val cursor = SponsorTable.select().where {
            SponsorTable.conferenceId EQ conferenceId
        }.execute()

        return mapToList(cursor)
    }

    fun existsById(name: String, group: String, conferenceId: Long): Boolean {
        val cursor = SponsorTable
            .select(COUNT())
            .where {
                (SponsorTable.name EQ name) AND (SponsorTable.groupName EQ group) AND (SponsorTable.conferenceId EQ conferenceId)
            }.limit(1)
            .execute()

        return cursor.use { it.getInt(0) > 0 }
    }

    fun sponsorsByGroup(group: String, conferenceId: Long): List<Sponsor> {
        val cursor = SponsorTable
            .select()
            .where {
                (SponsorTable.groupName EQ group) AND (SponsorTable.conferenceId EQ conferenceId)
            }.execute()

        return mapToList(cursor)
    }

    fun upsert(entity: Sponsor, conferenceId: Long) {
        SponsorTable
            .insert(onConflict = Action.REPLACE)
            .bind {
                it.name.bind(entity.id.name)
                it.groupName.bind(entity.id.group)
                it.conferenceId.bind(conferenceId)
                it.hasDetail.bind(entity.hasDetail)
                it.description.bind(entity.description)
                it.iconUrl.bind(entity.icon.string)
                it.url.bind(entity.url.string)
            }.execute()
    }

    fun deleteById(name: String, group: String, conferenceId: Long) {
        SponsorTable
            .delete()
            .where {
                (SponsorTable.name EQ name) AND (SponsorTable.groupName EQ group) AND (SponsorTable.conferenceId EQ conferenceId)
            }.execute()
    }
}
