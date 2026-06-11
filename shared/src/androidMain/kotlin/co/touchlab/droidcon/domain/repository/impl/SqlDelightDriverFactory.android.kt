package co.touchlab.droidcon.domain.repository.impl

import android.content.Context

actual class KQLitePathBuilder(private val context: Context) {
    actual fun buildPath(): String {
        return context.getDatabasePath("droidcon.db").absolutePath
    }
}
