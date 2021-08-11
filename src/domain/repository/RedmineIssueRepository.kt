package learn.kotlin.redmineevm.domain.repository

import learn.kotlin.redmineevm.domain.model.RedmineIssue

interface RedmineIssueRepository {
    fun getIssuesInJournalsByVersionId(version_id: Int) : List<RedmineIssue>
}