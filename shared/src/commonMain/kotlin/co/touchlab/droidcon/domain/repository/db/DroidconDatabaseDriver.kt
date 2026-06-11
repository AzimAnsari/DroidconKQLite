package co.touchlab.droidcon.domain.repository.db

import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.kqlite.database.KQLiteDriver

class DroidconDatabaseDriver : KQLiteDriver {

    private val bundledSQLiteDriver = BundledSQLiteDriver()

    override fun open(path: String, flags: Int?): SQLiteConnection {
        return flags?.let { bundledSQLiteDriver.open(path, it) } ?: bundledSQLiteDriver.open(path)
    }
}
