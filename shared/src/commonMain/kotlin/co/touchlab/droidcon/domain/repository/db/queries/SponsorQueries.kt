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

    fun sponsorById(name: String, group: String, conferenceId: Long): KQLiteCursor {
        return SponsorTable
            .select()
            .where {
                (SponsorTable.name EQ name) AND (SponsorTable.groupName EQ group) AND (SponsorTable.conferenceId EQ conferenceId)
            }.limit(1)
            .execute()
    }

    fun selectAll(conferenceId: Long): KQLiteCursor {
        return SponsorTable.select().where {
            SponsorTable.conferenceId EQ conferenceId
        }.execute()
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

    fun sponsorsByGroup(group: String, conferenceId: Long): KQLiteCursor {
        return SponsorTable
            .select()
            .where {
                (SponsorTable.groupName EQ group) AND (SponsorTable.conferenceId EQ conferenceId)
            }.execute()
    }

    fun upsert(entity: Sponsor, conferenceId: Long) {
        SponsorTable
            .insert(onConflict = Action.REPLACE)
            .bind {
                it.conferenceId.bind(conferenceId)
                it.binder(this, entity)
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
