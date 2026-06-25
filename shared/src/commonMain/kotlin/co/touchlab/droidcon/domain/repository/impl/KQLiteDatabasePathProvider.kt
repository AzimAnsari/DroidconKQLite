package co.touchlab.droidcon.domain.repository.impl

expect class KQLiteDatabasePathProvider {
    fun provideAbsolutePath(): String
}
