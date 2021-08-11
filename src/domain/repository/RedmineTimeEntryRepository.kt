package learn.kotlin.redmineevm.domain.repository

import learn.kotlin.redmineevm.domain.model.RedmineTimeEntry
import learn.kotlin.redmineevm.domain.value.IssueId

interface RedmineTimeEntryRepository {
    fun getTimeEntriesByIssueId(issueId: IssueId) : List<RedmineTimeEntry>
}