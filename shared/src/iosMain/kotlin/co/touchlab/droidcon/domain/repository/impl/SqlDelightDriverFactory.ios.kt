package co.touchlab.droidcon.domain.repository.impl

import co.touchlab.droidcon.domain.repository.db.DroidconDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class KQLitePathBuilder {
    actual fun buildPath(): String {
        val docs = NSFileManager.defaultManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
        val docsDir = docs.firstOrNull() as? NSURL ?: throw IllegalStateException("Documents directory not found.")
        return docsDir.URLByAppendingPathComponent(DroidconDatabase.NAME)?.path!!
    }
}
