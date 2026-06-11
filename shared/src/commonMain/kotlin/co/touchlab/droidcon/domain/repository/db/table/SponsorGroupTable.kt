package co.touchlab.droidcon.domain.repository.db.table

import co.touchlab.droidcon.domain.entity.SponsorGroup
import com.kqlite.column.notNull
import com.kqlite.constraint.ConstraintBuilder
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.table.KQLiteTable

object SponsorGroupTable : KQLiteTable("sponsorGroupTable") {
    val name = textColumn("name").notNull()
    val conferenceId = integerColumn("conferenceId").notNull()
    val displayPriority = intColumn("displayPriority").notNull()
    val prominent = booleanColumn("prominent").notNull()

    override val constraints: (ConstraintBuilder.() -> Unit)
        get() = {
            primaryKey(name, conferenceId)
            foreignKey(conferenceId).references(ConferenceTable.id)
        }

    fun mapSponsorGroup(cursor: KQLiteCursor): SponsorGroup {
        return SponsorGroup(
            id = SponsorGroup.Id(cursor[name]),
            displayPriority = cursor[displayPriority],
            isProminent = cursor[prominent],
        )
    }
}
