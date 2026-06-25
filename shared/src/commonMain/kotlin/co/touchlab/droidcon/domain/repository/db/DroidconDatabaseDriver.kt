package co.touchlab.droidcon.domain.repository.db

import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.kqlite.database.KQLiteDriver

class DroidconDatabaseDriver(private val sqLiteDriver: SQLiteDriver) : KQLiteDriver {

    override fun open(file: String, flags: Int?): SQLiteConnection {
        return if (flags != null && sqLiteDriver is BundledSQLiteDriver)
            sqLiteDriver.open(file, flags)
        else
            sqLiteDriver.open(file)
    }
}
