package co.touchlab.droidcon.domain.repository.db.table

import co.touchlab.droidcon.domain.entity.Room
import com.kqlite.column.Bind
import com.kqlite.column.notNull
import com.kqlite.constraint.ConstraintBuilder
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.table.KQLiteAdapter
import com.kqlite.table.KQLiteTable

object RoomTable : KQLiteTable("roomTable"), KQLiteAdapter<Room> {
    val id = integerColumn("id").notNull()
    val conferenceId = integerColumn("conferenceId").notNull()
    val name = textColumn("name").notNull()

    override val constraints: (ConstraintBuilder.() -> Unit)
        get() = {
            primaryKey(id, conferenceId)
            foreignKey(conferenceId).references(ConferenceTable.id)
        }

    override fun binder(bind: Bind, item: Room) {
        bind.apply {
            id.bind(item.id.value)
            name.bind(item.name)
        }
    }

    override fun mapper(cursor: KQLiteCursor): Room {
        return Room(id = Room.Id(cursor[id]), name = cursor[name])
    }
}
