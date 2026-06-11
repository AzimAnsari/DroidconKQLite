package co.touchlab.droidcon.domain.repository.db.queries

import co.touchlab.droidcon.domain.entity.Room
import co.touchlab.droidcon.domain.repository.db.table.RoomTable
import co.touchlab.droidcon.domain.repository.db.table.SessionTable
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.functions.COUNT
import com.kqlite.statement.quickDelete
import com.kqlite.statement.quickInsert
import com.kqlite.statement.quickSelect
import com.kqlite.statement.select
import com.kqlite.statement.update
import com.kqlite.table.Action

class RoomQueries {

    private fun mapToList(cursor: KQLiteCursor): List<Room> =
        cursor.use {
            it.asSequence().map(RoomTable::mapRoom).toList()
        }

    fun selectAll(conferenceId: Long): List<Room> {
        val cursor = RoomTable.quickSelect(
            where = { it.conferenceId EQ conferenceId },
        )
        return mapToList(cursor)
    }

    fun selectById(id: Long, conferenceId: Long): Room? {
        val cursor = RoomTable.quickSelect(
            where = {
                (it.id EQ id).AND(it.conferenceId EQ conferenceId)
            },
        )
        return mapToList(cursor).firstOrNull()
    }

    fun upsert(id: Long, conferenceId: Long, name: String) {
        RoomTable.quickInsert(onConflict = Action.REPLACE) {
            it.id.bind(id)
            it.conferenceId.bind(conferenceId)
            it.name.bind(name)
        }
    }

    fun deleteById(id: Long, conferenceId: Long) {
        SessionTable.update {
            it.roomId.bind(null)
        }.where {
            (it.roomId EQ id) AND
                (it.conferenceId EQ conferenceId)
        }.execute()

        RoomTable.quickDelete {
            it.id.EQ(id) AND
                it.conferenceId.EQ(conferenceId)
        }
    }

    fun existsById(id: Long, conferenceId: Long): Boolean {
        val cursor = RoomTable
            .select(COUNT())
            .where {
                (it.id EQ id).AND(it.conferenceId EQ conferenceId)
            }.execute()

        return cursor.use { it.getInt(0) > 0 }
    }
}
