package co.touchlab.droidcon.domain.repository.db

import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import co.touchlab.droidcon.domain.repository.db.queries.ConferenceQueries
import co.touchlab.droidcon.domain.repository.db.queries.ProfileQueries
import co.touchlab.droidcon.domain.repository.db.queries.RoomQueries
import co.touchlab.droidcon.domain.repository.db.queries.SessionQueries
import co.touchlab.droidcon.domain.repository.db.queries.SessionSpeakerQueries
import co.touchlab.droidcon.domain.repository.db.queries.SponsorGroupQueries
import co.touchlab.droidcon.domain.repository.db.queries.SponsorQueries
import co.touchlab.droidcon.domain.repository.db.queries.SponsorRepresentativeQueries
import co.touchlab.droidcon.domain.repository.db.table.ConferenceTable
import co.touchlab.droidcon.domain.repository.db.table.ProfileTable
import co.touchlab.droidcon.domain.repository.db.table.RoomTable
import co.touchlab.droidcon.domain.repository.db.table.SessionSpeakerTable
import co.touchlab.droidcon.domain.repository.db.table.SessionTable
import co.touchlab.droidcon.domain.repository.db.table.SponsorGroupTable
import co.touchlab.droidcon.domain.repository.db.table.SponsorRepresentativeTable
import co.touchlab.droidcon.domain.repository.db.table.SponsorTable
import co.touchlab.droidcon.domain.repository.impl.KQLiteDatabasePathProvider
import com.kqlite.database.KQLiteDatabase
import com.kqlite.pragma.JournalMode
import com.kqlite.pragma.KQLitePragma
import com.kqlite.statement.quickInsert
import com.kqlite.table.KQLiteTable

class DroidconDatabase(pathBuilder: KQLiteDatabasePathProvider) : KQLiteDatabase(
    kqLiteDriver = DroidconDatabaseDriver(dbFile = pathBuilder.provideAbsolutePath(), version = VERSION, BundledSQLiteDriver()),
) {

    companion object {
        const val NAME = "droidcon.db"
        const val VERSION = 1
    }

    override fun getKQLiteTables(): List<KQLiteTable> {
        return listOf(
            ConferenceTable,
            ProfileTable,
            RoomTable,
            SessionTable,
            SessionSpeakerTable,
            SponsorTable,
            SponsorGroupTable,
            SponsorRepresentativeTable,
        )
    }

    override fun onConfigure(pragma: KQLitePragma) {
        pragma.journalMode.set(JournalMode.WAL)
    }

    override fun onCreate(connection: SQLiteConnection) {
        ConferenceTable.quickInsert {
            it.conferenceName.bind("Droidcon NYC 2025")
            it.conferenceTimeZone.bind("America/New_York")
            it.projectId.bind("droidcon-148cc")
            it.collectionName.bind("sponsors-nyc-2025")
            it.apiKey.bind("AIzaSyCkD5DH2rUJ8aZuJzANpIFj0AVuCNik1l0")
            it.scheduleId.bind("4lffd9w7")
            it.selected.bind(true)
            it.active.bind(true)
            it.venueMap.bind(null)
        }
    }

    val profileQueries = ProfileQueries()
    val sessionQueries = SessionQueries()
    val roomQueries = RoomQueries()
    val sponsorQueries = SponsorQueries()
    val sponsorGroupQueries = SponsorGroupQueries()
    val sessionSpeakerQueries = SessionSpeakerQueries()
    val conferenceQueries = ConferenceQueries()
    val sponsorRepresentativeQueries = SponsorRepresentativeQueries()
}
