package learn.kotlin.redmineevm.infrastructure.externalapi

import com.taskadapter.redmineapi.Include
import com.taskadapter.redmineapi.bean.Issue
import com.taskadapter.redmineapi.internal.ResultsWrapper
import learn.kotlin.redmineevm.domain.model.RedmineIssue
import learn.kotlin.redmineevm.domain.model.RedmineJournal
import learn.kotlin.redmineevm.domain.repository.RedmineIssueRepository
import learn.kotlin.redmineevm.domain.value.*
import org.apache.commons.lang3.time.DateUtils
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class RedmineIssueApi (uri: String, apikey: String) : RedmineIssueRepository, RedmineBaseApi(uri, apikey){
    override fun getIssuesInJournalsByVersionId(version_id: Int) : List<RedmineIssue> {
        val issueMgr = mgr.issueManager
        val params: MutableMap<String, String> = HashMap()
        params["fixed_version_id"] = version_id.toString()
        params["offset"] = "0"
        params["limit"] = "100"
        params["status_id"] = "*"
        val issues: ResultsWrapper<Issue>? = issueMgr.getIssues(params)
        issues ?: throw RuntimeException("Issue is not found")

        val redmineIssues : List<RedmineIssue> = issues.results.map { issue ->
            val issueInJournal = issueMgr.getIssueById(issue.id, Include.journals)
            val journals = mutableListOf<RedmineJournal>()
            val isoFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            isoFormat.timeZone = TimeZone.getTimeZone("JST")
            issueInJournal.journals.forEach { journal ->
                journal.details.filter { detail ->
                    detail.name == "done_ratio"
                }.map { detail ->
                    RedmineJournal(
                        CreateOn(LocalDateTime.from(DateUtils.truncate(journal.createdOn, Calendar.DAY_OF_MONTH).toInstant().atZone(ZoneId.systemDefault())).toLocalDate()),
                        DoneRatio(detail.newValue.toInt())
                    )
                }.forEach { journals.add(it) }
            }

            RedmineIssue(
                IssueId(issue.id),
                issue.parentId?.let { IssueId(it) },
                StartDate(LocalDateTime.from(DateUtils.truncate(issue.startDate, Calendar.DAY_OF_MONTH).toInstant().atZone(ZoneId.systemDefault())).toLocalDate()),
                issue.dueDate?.let { DueDate(LocalDateTime.from(DateUtils.truncate(issue.dueDate, Calendar.DAY_OF_MONTH).toInstant().atZone(ZoneId.systemDefault())).toLocalDate()) },
                issue.closedOn?.let { CloseOn(LocalDateTime.from(DateUtils.truncate(issue.closedOn, Calendar.DAY_OF_MONTH).toInstant().atZone(ZoneId.systemDefault())).toLocalDate()) },
                issue.estimatedHours?.let { EstimatedHour(it) },
                journals,
            )
        }

        return redmineIssues
    }
}