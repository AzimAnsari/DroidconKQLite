package co.touchlab.droidcon.domain.repository.db.table

import co.touchlab.droidcon.domain.entity.Room
import co.touchlab.droidcon.domain.entity.Session
import co.touchlab.droidcon.domain.service.DateTimeService
import com.kqlite.column.Bind
import com.kqlite.column.UnixEpochLiteral
import com.kqlite.column.notNull
import com.kqlite.constraint.ConstraintBuilder
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.table.KQLiteTable
import kotlin.time.Instant

object SessionTable : KQLiteTable("sessionTable") {
    val id = textColumn("id").notNull()
    val conferenceId = integerColumn("conferenceId").notNull()
    val title = textColumn("title").notNull()
    val description = textColumn("description")
    val startsAt = unixEpochColumn("startsAt").notNull()
    val endsAt = unixEpochColumn("endsAt").notNull()
    val serviceSession = booleanColumn("serviceSession").notNull().default(false)
    val rsvp = booleanColumn("rsvp")
    val rsvpSent = booleanColumn("rsvpSent").notNull().default(false)
    val roomId = integerColumn("roomId")
    val feedbackRating = intColumn("feedbackRating")
    val feedbackComment = textColumn("feedbackComment")
    val feedbackSent = booleanColumn("feedbackSent").notNull().default(false)

    override val constraints: (ConstraintBuilder.() -> Unit)
        get() = {
            primaryKey(id, conferenceId)
            foreignKey(roomId).references(RoomTable.id)
            foreignKey(conferenceId).references(ConferenceTable.id)
        }

    lateinit var dateTimeService: DateTimeService

    fun mapSession(cursor: KQLiteCursor): Session {
        return Session(
            dateTimeService = dateTimeService,
            id = Session.Id(cursor[id]),
            title = cursor[title],
            description = cursor[description],
            startsAt = Instant.fromEpochMilliseconds(cursor[startsAt].getLong()),
            endsAt = Instant.fromEpochMilliseconds(cursor[endsAt].getLong()),
            isServiceSession = cursor[serviceSession],
            room = cursor[roomId]?.let(Room::Id),
            rsvp = Session.RSVP(cursor[rsvp] ?: false, cursor[rsvpSent]),
            feedback = run {
                val rating = cursor[feedbackRating]
                val comment = cursor[feedbackComment]
                if (rating != null && comment != null) {
                    Session.Feedback(rating, comment, cursor[feedbackSent])
                } else {
                    null
                }
            },
        )
    }

    fun bindSession(bind: Bind, session: Session, confId: Long) {
        with(bind) {
            id.bind(session.id.value)
            conferenceId.bind(confId)
            title.bind(session.title)
            description.bind(session.description)
            startsAt.bind(UnixEpochLiteral(session.startsAt.toEpochMilliseconds()))
            endsAt.bind(UnixEpochLiteral(session.endsAt.toEpochMilliseconds()))
            serviceSession.bind(session.isServiceSession)
            rsvp.bind(session.rsvp.isAttending)
            rsvpSent.bind(session.rsvp.isSent)
            roomId.bind(session.room?.value)
            feedbackRating.bind(session.feedback?.rating)
            feedbackComment.bind(session.feedback?.comment)
            feedbackSent.bind(session.feedback?.isSent ?: false)
        }
    }
}
