package co.touchlab.droidcon.domain.repository.db.table

import co.touchlab.droidcon.composite.Url
import co.touchlab.droidcon.domain.entity.Sponsor
import com.kqlite.column.Bind
import com.kqlite.column.notNull
import com.kqlite.constraint.ConstraintBuilder
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.table.KQLiteAdapter
import com.kqlite.table.KQLiteTable

object SponsorTable : KQLiteTable("sponsorTable"), KQLiteAdapter<Sponsor> {
    val name = textColumn("name").notNull()
    val groupName = textColumn("groupName").notNull()
    val conferenceId = integerColumn("conferenceId").notNull()
    val hasDetail = booleanColumn("hasDetail").notNull()
    val description = textColumn("description")
    val iconUrl = textColumn("iconUrl").notNull()
    val url = textColumn("url").notNull()

    override val constraints: (ConstraintBuilder.() -> Unit)
        get() = {
            primaryKey(name, groupName, conferenceId)
            foreignKey(groupName, conferenceId).references(SponsorGroupTable.name, SponsorGroupTable.conferenceId)
            foreignKey(conferenceId).references(ConferenceTable.id)
        }

    override fun binder(bind: Bind, item: Sponsor) {
        bind.apply {
            name.bind(item.id.name)
            groupName.bind(item.id.group)
            hasDetail.bind(item.hasDetail)
            description.bind(item.description)
            iconUrl.bind(item.icon.string)
            url.bind(item.url.string)
        }
    }

    override fun mapper(cursor: KQLiteCursor): Sponsor {
        return Sponsor(
            id = Sponsor.Id(cursor[name], cursor[groupName]),
            hasDetail = cursor[hasDetail],
            description = cursor[description],
            icon = Url(cursor[iconUrl]),
            url = Url(cursor[url]),
        )
    }
}
