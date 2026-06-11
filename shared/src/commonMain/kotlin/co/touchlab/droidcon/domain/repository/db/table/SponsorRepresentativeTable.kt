package co.touchlab.droidcon.domain.repository.db.table

import com.kqlite.column.notNull
import com.kqlite.constraint.ConstraintBuilder
import com.kqlite.table.KQLiteTable

object SponsorRepresentativeTable: KQLiteTable("sponsorRepresentativeTable") {
    val sponsorName = textColumn("sponsorName").notNull()
    val sponsorGroupName = textColumn("sponsorGroupName").notNull()
    val representativeId = textColumn("representativeId").notNull()
    val conferenceId = integerColumn("conferenceId").notNull()
    val displayOrder = integerColumn("displayOrder").notNull().default(0)

    override val constraints: (ConstraintBuilder.() -> Unit)
        get() = {
            primaryKey(sponsorName, sponsorGroupName, representativeId, conferenceId)
            foreignKey(sponsorName, sponsorGroupName, conferenceId).references(SponsorTable.name, SponsorTable.groupName, SponsorTable.conferenceId)
            foreignKey(representativeId, conferenceId).references(ProfileTable.id, ProfileTable.conferenceId)
            foreignKey(conferenceId).references(ConferenceTable.id)
        }

}
