package co.touchlab.droidcon.domain.repository.db.queries

import co.touchlab.droidcon.domain.entity.Conference
import co.touchlab.droidcon.domain.repository.db.table.ConferenceTable
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.statement.delete
import com.kqlite.statement.insert
import com.kqlite.statement.select
import com.kqlite.statement.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class ConferenceQueries {
    private var allUpdate: (() -> Job)? = null
    private var selectedUpdate: (() -> Job)? = null

    fun mapToList(cursor: KQLiteCursor): List<Conference> =
        cursor.use {
            it.asSequence().map(ConferenceTable::mapConference).toList()
        }

    fun observeAllActive(): Flow<List<Conference>> = callbackFlow {
        fun queryAndSend() {
            val cursor = ConferenceTable
                .select()
                .where { it.active EQ true }
                .execute()

            val list = mapToList(cursor)
            trySend(list)
        }

        queryAndSend()

        val listener = { launch { queryAndSend() } }
        allUpdate = listener
        awaitClose { allUpdate = null }
    }

    fun observeSelected(): Flow<Conference> = callbackFlow {
        fun queryAndSend() {
            val cursor = ConferenceTable
                .select()
                .where { it.selected EQ true }
                .limit(1)
                .execute()
            val conference = mapToList(cursor).first()
            trySend(conference)
        }

        queryAndSend()

        val listener = { launch { queryAndSend() } }
        selectedUpdate = listener
        awaitClose { selectedUpdate = null }
    }

    fun selectSelected(): Conference {
        val cursor = ConferenceTable
            .select()
            .where { it.selected EQ true }
            .limit(1)
            .execute()
        return mapToList(cursor).first()
    }

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

        allUpdate?.invoke()
        selectedUpdate?.invoke()
    }

    fun insertConference(conference: Conference): Long {
        val id = ConferenceTable
            .insert()
            .bind {
                it.bindConference(this, conference)
            }.executeReturning(ConferenceTable.id)
        allUpdate?.invoke()
        selectedUpdate?.invoke()
        return id
    }

    fun updateConference(conference: Conference): Boolean {
        val cursor = ConferenceTable
            .update {
                it.bindConference(this, conference)
            }.where {
                it.id EQ conference.id
            }.executeReturning(ConferenceTable.id)
        val count = cursor.use {
            it.asSequence().count()
        }
        allUpdate?.invoke()
        selectedUpdate?.invoke()
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
        allUpdate?.invoke()
        selectedUpdate?.invoke()
        return count > 0
    }
}
