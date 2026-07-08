package co.touchlab.droidcon.domain.repository.db

import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.kqlite.database.KQLiteDriver

class DroidconDatabaseDriver(
    override val dbFile: String,
    override val version: Int,
    private val sqLiteDriver: SQLiteDriver,
) : KQLiteDriver {

    override fun open(flags: Int?): SQLiteConnection {
        return if (flags != null && sqLiteDriver is BundledSQLiteDriver)
            sqLiteDriver.open(dbFile, flags)
        else
            sqLiteDriver.open(dbFile)
    }
}
