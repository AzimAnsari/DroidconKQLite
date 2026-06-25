package co.touchlab.droidcon.domain.repository.impl

import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class KQLiteDatabasePathProvider {
    actual fun provideAbsolutePath(): String {
        val fileManager = NSFileManager.defaultManager
        val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
        val documentDirectory = urls.first() as NSURL
        return documentDirectory.path!!
    }
}
