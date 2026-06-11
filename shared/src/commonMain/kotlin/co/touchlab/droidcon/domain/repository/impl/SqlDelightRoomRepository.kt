package co.touchlab.droidcon.domain.repository.impl

import co.touchlab.droidcon.domain.entity.Room
import co.touchlab.droidcon.domain.repository.RoomRepository
import co.touchlab.droidcon.domain.repository.db.queries.RoomQueries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SqlDelightRoomRepository(private val roomQueries: RoomQueries) :
    BaseRepository<Room.Id, Room>(),
    RoomRepository {

    override fun allSync(conferenceId: Long): List<Room> = roomQueries.selectAll(conferenceId)

    override fun observe(id: Room.Id, conferenceId: Long): Flow<Room> =
        flow { emit(roomQueries.selectById(id.value, conferenceId)!!) }

    override fun observeOrNull(id: Room.Id, conferenceId: Long): Flow<Room?> =
        flow { emit(roomQueries.selectById(id.value, conferenceId)) }

    override fun observeAll(conferenceId: Long): Flow<List<Room>> =
        flow { emit(roomQueries.selectAll(conferenceId)) }

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
