package co.touchlab.droidcon.domain.repository.db.table

import co.touchlab.droidcon.composite.Url
import co.touchlab.droidcon.domain.entity.Profile
import com.kqlite.column.Bind
import com.kqlite.column.notNull
import com.kqlite.constraint.ConstraintBuilder
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.table.KQLiteAdapter
import com.kqlite.table.KQLiteTable

object ProfileTable : KQLiteTable("profileTable"), KQLiteAdapter<Profile> {
    val id = textColumn("id").notNull()
    val conferenceId = integerColumn("conferenceId").notNull()
    val fullName = textColumn("fullName").notNull()
    val bio = textColumn("bio")
    val tagLine = textColumn("tagLine")
    val profilePicture = textColumn("profilePicture")
    val twitter = textColumn("twitter")
    val linkedIn = textColumn("linkedIn")
    val website = textColumn("website")

    override val constraints: (ConstraintBuilder.() -> Unit)
        get() = {
            primaryKey(id, conferenceId)
            foreignKey(conferenceId).references(ConferenceTable.id)
        }

    override fun binder(bind: Bind, item: Profile) {
        bind.apply {
            id.bind(item.id.value)
            fullName.bind(item.fullName)
            bio.bind(item.bio)
            tagLine.bind(item.tagLine)
            profilePicture.bind(item.profilePicture?.string)
            twitter.bind(item.twitter?.string)
            linkedIn.bind(item.linkedIn?.string)
            website.bind(item.website?.string)
        }
    }

    override fun mapper(cursor: KQLiteCursor): Profile {
        return Profile(
            id = Profile.Id(cursor[id]),
            fullName = cursor[fullName],
            bio = cursor[bio],
            tagLine = cursor[tagLine],
            profilePicture = cursor[profilePicture]?.let(::Url),
            twitter = cursor[twitter]?.let(::Url),
            linkedIn = cursor[linkedIn]?.let(::Url),
            website = cursor[website]?.let(::Url),
        )
    }
}
