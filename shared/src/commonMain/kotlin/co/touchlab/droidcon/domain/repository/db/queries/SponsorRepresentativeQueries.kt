package co.touchlab.droidcon.domain.repository.db.queries

import co.touchlab.droidcon.domain.repository.db.table.SponsorRepresentativeTable
import com.kqlite.statement.quickDelete
import com.kqlite.statement.quickInsert
import com.kqlite.table.Action

class SponsorRepresentativeQueries {
    fun deleteBySponsorId(sponsorName: String, sponsorGroupName: String, conferenceId: Long) {
        SponsorRepresentativeTable.quickDelete {
            (it.sponsorName EQ sponsorName) AND
                (it.sponsorGroupName EQ sponsorGroupName) AND
                (it.conferenceId EQ conferenceId)
        }
    }

    fun insertUpdate(sponsorName: String, sponsorGroupName: String, representativeId: String, conferenceId: Long, displayOrder: Long) {
        SponsorRepresentativeTable
            .quickInsert(onConflict = Action.REPLACE) {
                it.sponsorName.bind(sponsorName)
                it.sponsorGroupName.bind(sponsorGroupName)
                it.representativeId.bind(representativeId)
                it.conferenceId.bind(conferenceId)
                it.displayOrder.bind(displayOrder)
            }
    }
}
