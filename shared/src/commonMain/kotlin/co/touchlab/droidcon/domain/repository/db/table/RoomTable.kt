package co.touchlab.droidcon.domain.repository.db.table

import co.touchlab.droidcon.domain.entity.Room
import com.kqlite.column.notNull
import com.kqlite.constraint.ConstraintBuilder
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.table.KQLiteTable

object RoomTable : KQLiteTable("roomTable") {
    val id = integerColumn("id").notNull()
    val conferenceId = integerColumn("conferenceId").notNull()
    val name = textColumn("name").notNull()

    override val constraints: (ConstraintBuilder.() -> Unit)
        get() = {
            primaryKey(id, conferenceId)
            foreignKey(conferenceId).references(ConferenceTable.id)
        }

    fun mapRoom(cursor: KQLiteCursor): Room {
        return Room(id = Room.Id(cursor[id]), name = cursor[name])
    }
}
