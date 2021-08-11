package learn.kotlin.redmineevm.infrastructure.externalapi

import com.taskadapter.redmineapi.bean.TimeEntry
import com.taskadapter.redmineapi.internal.ResultsWrapper
import learn.kotlin.redmineevm.domain.model.RedmineTimeEntry
import learn.kotlin.redmineevm.domain.repository.RedmineTimeEntryRepository
import learn.kotlin.redmineevm.domain.value.ActualCost
import learn.kotlin.redmineevm.domain.value.IssueId
import learn.kotlin.redmineevm.domain.value.SpentOn
import org.apache.commons.lang3.time.DateUtils
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap

/**
 *
 */
class RedmineTimeEntryApi(uri: String, apikey: String) : RedmineTimeEntryRepository, RedmineBaseApi(uri, apikey) {
    override fun getTimeEntriesByIssueId(issueId: IssueId) : List<RedmineTimeEntry> {
        val timeEntryMgr = mgr.timeEntryManager
        val params: MutableMap<String, String> = HashMap()
        params["issue_id"] = issueId.value.toString()
        params["offset"] = "0"
        params["limit"] = "100"
        val timeEntries: ResultsWrapper<TimeEntry>? = timeEntryMgr.getTimeEntries(params)
        timeEntries ?: throw RuntimeException("TimeEntries is not found")

        val redmineIssues : List<RedmineTimeEntry> = timeEntries.results.map { timeEntry ->
            RedmineTimeEntry(
                IssueId(timeEntry.issueId),
                SpentOn(LocalDateTime.from(DateUtils.truncate(timeEntry.spentOn, Calendar.DAY_OF_MONTH).toInstant().atZone(ZoneId.systemDefault())).toLocalDate()),
                ActualCost(timeEntry.hours)
            )
        }

        return redmineIssues
    }
}