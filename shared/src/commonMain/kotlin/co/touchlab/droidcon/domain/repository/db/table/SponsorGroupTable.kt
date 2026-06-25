package co.touchlab.droidcon.domain.repository.db.table

import co.touchlab.droidcon.domain.entity.SponsorGroup
import com.kqlite.column.Bind
import com.kqlite.column.notNull
import com.kqlite.constraint.ConstraintBuilder
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.table.KQLiteAdapter
import com.kqlite.table.KQLiteTable

object SponsorGroupTable : KQLiteTable("sponsorGroupTable"), KQLiteAdapter<SponsorGroup> {
    val name = textColumn("name").notNull()
    val conferenceId = integerColumn("conferenceId").notNull()
    val displayPriority = intColumn("displayPriority").notNull()
    val prominent = booleanColumn("prominent").notNull()

    override val constraints: (ConstraintBuilder.() -> Unit)
        get() = {
            primaryKey(name, conferenceId)
            foreignKey(conferenceId).references(ConferenceTable.id)
        }

    override fun binder(bind: Bind, item: SponsorGroup) {
        bind.apply {
            name.bind(item.name)
            displayPriority.bind(item.displayPriority)
            prominent.bind(item.isProminent)
        }
    }

    override fun mapper(cursor: KQLiteCursor): SponsorGroup {
        return SponsorGroup(
            id = SponsorGroup.Id(cursor[name]),
            displayPriority = cursor[displayPriority],
            isProminent = cursor[prominent],
        )
    }
}
