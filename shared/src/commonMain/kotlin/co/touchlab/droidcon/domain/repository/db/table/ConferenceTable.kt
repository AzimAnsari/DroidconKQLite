package co.touchlab.droidcon.domain.repository.db.table

import co.touchlab.droidcon.domain.entity.Conference
import com.kqlite.column.Bind
import com.kqlite.column.notNull
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.table.KQLiteAdapter
import com.kqlite.table.KQLiteTable
import kotlinx.datetime.TimeZone

object ConferenceTable : KQLiteTable("conferenceTable"), KQLiteAdapter<Conference> {
    val id = integerColumn("id").notNull().primaryKey().autoIncrement()
    val conferenceName = textColumn("conferenceName").notNull()
    val conferenceTimeZone = textColumn("conferenceTimeZone").notNull()
    val projectId = textColumn("projectId").notNull()
    val collectionName = textColumn("collectionName").notNull()
    val apiKey = textColumn("apiKey").notNull()
    val scheduleId = textColumn("scheduleId").notNull()
    val selected = booleanColumn("selected").notNull().default(false)
    val active = booleanColumn("active").notNull().default(true)
    val venueMap = textColumn("venueMap")

    override fun binder(bind: Bind, item: Conference) {
        bind.apply {
            conferenceName.bind(item.name)
            conferenceTimeZone.bind(item.timeZone.id)
            projectId.bind(item.projectId)
            collectionName.bind(item.collectionName)
            apiKey.bind(item.apiKey)
            scheduleId.bind(item.scheduleId)
            selected.bind(item.selected)
            active.bind(item.active)
            venueMap.bind(item.venueMap)
        }
    }

    override fun mapper(cursor: KQLiteCursor): Conference {
        return Conference(
            _id = cursor[id],
            name = cursor[conferenceName],
            timeZone = TimeZone.of(cursor[conferenceTimeZone]),
            projectId = cursor[projectId],
            collectionName = cursor[collectionName],
            apiKey = cursor[apiKey],
            scheduleId = cursor[scheduleId],
            selected = cursor[selected],
            active = cursor[active],
            venueMap = cursor[venueMap],
        )
    }
}
