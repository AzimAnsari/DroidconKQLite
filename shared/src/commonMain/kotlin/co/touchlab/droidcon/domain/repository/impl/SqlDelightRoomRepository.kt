package co.touchlab.droidcon.domain.repository.impl

import co.touchlab.droidcon.domain.entity.Room
import co.touchlab.droidcon.domain.repository.RoomRepository
import co.touchlab.droidcon.domain.repository.db.queries.RoomQueries
import co.touchlab.droidcon.domain.repository.db.table.RoomTable
import com.kqlite.cursor.asCallbackFlow
import com.kqlite.cursor.mapToList
import com.kqlite.cursor.mapToSingle
import com.kqlite.cursor.mapToSingleOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class SqlDelightRoomRepository(private val roomQueries: RoomQueries) :
    BaseRepository<Room.Id, Room>(),
    RoomRepository {

    override fun allSync(conferenceId: Long): List<Room> = roomQueries.selectAll(conferenceId).mapToList(RoomTable::mapper)

    override fun observe(id: Room.Id, conferenceId: Long): Flow<Room> =
        roomQueries.selectById(id.value, conferenceId).asCallbackFlow().mapToSingle(Dispatchers.Main, RoomTable::mapper)

    override fun observeOrNull(id: Room.Id, conferenceId: Long): Flow<Room?> =
        roomQueries.selectById(id.value, conferenceId).asCallbackFlow().mapToSingleOrNull(Dispatchers.Main, RoomTable::mapper)

    override fun observeAll(conferenceId: Long): Flow<List<Room>> =
        roomQueries.selectAll(conferenceId).asCallbackFlow().mapToList(Dispatchers.Main, RoomTable::mapper)

    override fun doUpsert(entity: Room, conferenceId: Long) {
        roomQueries.upsert(
            id = entity.id.value,
            conferenceId = conferenceId,
            name = entity.name,
        )
    }

    override fun doDelete(id: Room.Id, conferenceId: Long) {
        roomQueries.deleteById(id.value, conferenceId)
    }

    override fun contains(id: Room.Id, conferenceId: Long): Boolean = roomQueries.existsById(id.value, conferenceId)
}
