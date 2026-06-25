package co.touchlab.droidcon.domain.repository.impl

import android.content.Context
import co.touchlab.droidcon.domain.repository.db.DroidconDatabase

actual class KQLiteDatabasePathProvider(private val context: Context) {
    actual fun provideAbsolutePath(): String = context.getDatabasePath(DroidconDatabase.NAME).absolutePath
}
