package co.touchlab.droidcon.domain.repository.db.table

import co.touchlab.droidcon.domain.entity.Conference
import com.kqlite.column.Bind
import com.kqlite.column.notNull
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.table.KQLiteTable
import kotlinx.datetime.TimeZone

object ConferenceTable : KQLiteTable("conferenceTable") {
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

    fun mapConference(cursor: KQLiteCursor): Conference {
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

    fun bindConference(bind: Bind, conference: Conference) {
        with(bind) {
            conferenceName.bind(conference.name)
            conferenceTimeZone.bind(conference.timeZone.id)
            projectId.bind(conference.projectId)
            collectionName.bind(conference.collectionName)
            apiKey.bind(conference.apiKey)
            scheduleId.bind(conference.scheduleId)
            selected.bind(conference.selected)
            active.bind(conference.active)
            venueMap.bind(conference.venueMap)
        }
    }
}
