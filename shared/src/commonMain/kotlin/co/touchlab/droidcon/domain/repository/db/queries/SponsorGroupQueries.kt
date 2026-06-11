package co.touchlab.droidcon.domain.repository.db.queries

import co.touchlab.droidcon.domain.entity.SponsorGroup
import co.touchlab.droidcon.domain.repository.db.table.SponsorGroupTable
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.functions.COUNT
import com.kqlite.statement.delete
import com.kqlite.statement.insert
import com.kqlite.statement.quickSelect
import com.kqlite.statement.select
import com.kqlite.table.Action

class SponsorGroupQueries {

    private fun mapSponsorGroup(cursor: KQLiteCursor): List<SponsorGroup> =
        cursor.use {
            it.asSequence().map(SponsorGroupTable::mapSponsorGroup).toList()
        }

    fun selectAll(conferenceId: Long): List<SponsorGroup> {
        val cursor = SponsorGroupTable.quickSelect(
            where = { it.conferenceId EQ conferenceId },
        )
        return mapSponsorGroup(cursor)
    }

    fun sponsorGroupByName(name: String, conferenceId: Long): SponsorGroup? {
        val cursor = SponsorGroupTable
            .select()
            .where {
                (it.name EQ name) AND (it.conferenceId EQ conferenceId)
            }.limit(1)
            .execute()

        return mapSponsorGroup(cursor).firstOrNull()
    }

    fun existsByName(name: String, conferenceId: Long): Boolean {
        return SponsorGroupTable
            .select(COUNT())
            .where {
                (it.name EQ name) AND (it.conferenceId EQ conferenceId)
            }.limit(1)
            .execute()
            .use {
                it.getInt(0) > 0
            }
    }

    fun upsert(entity: SponsorGroup, conferenceId: Long) {
        SponsorGroupTable
            .insert(onConflict = Action.REPLACE)
            .bind {
                it.name.bind(entity.id.value)
                it.conferenceId.bind(conferenceId)
                it.displayPriority.bind(entity.displayPriority)
                it.prominent.bind(entity.isProminent)
            }.execute()
    }

    fun deleteByName(name: String, conferenceId: Long) {
        SponsorGroupTable
            .delete()
            .where {
                (it.name EQ name) AND (it.conferenceId EQ conferenceId)
            }.execute()
    }
}
