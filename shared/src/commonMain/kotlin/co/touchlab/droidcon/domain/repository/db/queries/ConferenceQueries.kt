package co.touchlab.droidcon.domain.repository.db.queries

import co.touchlab.droidcon.domain.entity.Conference
import co.touchlab.droidcon.domain.repository.db.table.ConferenceTable
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.statement.delete
import com.kqlite.statement.insert
import com.kqlite.statement.quickSelect
import com.kqlite.statement.select
import com.kqlite.statement.update

class ConferenceQueries {

    fun selectAllActive(): KQLiteCursor = ConferenceTable.quickSelect(where = { it.active EQ true })

    fun selectSelected(): KQLiteCursor = ConferenceTable
        .select()
        .where { it.selected EQ true }
        .limit(1)
        .execute()

    fun changeSelectedConference(conferenceId: Long) {
        ConferenceTable
            .update {
                it.selected.bind(false)
            }.where {
                it.selected EQ true
            }.execute()

        ConferenceTable
            .update {
                it.selected.bind(true)
            }.where {
                it.id EQ conferenceId
            }.execute()
    }

    fun insert(conference: Conference): Long {
        return ConferenceTable
            .insert()
            .bind {
                it.binder(this, conference)
            }.use {
                it.executeReturning(ConferenceTable.id)
            }
    }

    fun updateConference(conference: Conference): Boolean {
        val cursor = ConferenceTable
            .update {
                it.binder(this, conference)
            }.where {
                it.id EQ conference.id
            }.executeReturning(ConferenceTable.id)
        val count = cursor.use {
            it.asSequence().count()
        }
        return count > 0
    }

    fun deleteById(conferenceId: Long): Boolean {
        val cursor = ConferenceTable
            .delete()
            .where {
                it.id EQ conferenceId
            }.executeReturning(ConferenceTable.id)
        val count = cursor.use {
            it.asSequence().count()
        }
        return count > 0
    }
}
