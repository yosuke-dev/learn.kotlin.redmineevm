package learn.kotlin.redmineevm.application.service

import learn.kotlin.redmineevm.domain.model.EarnedSchedule
import learn.kotlin.redmineevm.domain.model.RedmineTimeEntry
import learn.kotlin.redmineevm.domain.repository.RedmineIssueRepository
import learn.kotlin.redmineevm.domain.repository.RedmineTimeEntryRepository
import learn.kotlin.redmineevm.domain.service.DoneRatioService
import learn.kotlin.redmineevm.domain.value.*
import learn.kotlin.redmineevm.infrastructure.externalapi.RedmineIssueApi
import learn.kotlin.redmineevm.infrastructure.externalapi.RedmineTimeEntryApi
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.math.floor

class EarnedScheduleManagement(private val redmineIssueApi: RedmineIssueRepository, private val redmineTimeEntryApi: RedmineTimeEntryRepository) {
    fun getEarnedValueSchedules(versionId: Int): List<EarnedSchedule> {
        val issues = redmineIssueApi.getIssuesInJournalsByVersionId(versionId)
        val timeEntries = mutableListOf<RedmineTimeEntry>()

        issues.forEach{ timeEntries.addAll(redmineTimeEntryApi.getTimeEntriesByIssueId(it.id)) }

        val beginDate = minOf(timeEntries.minOf { it.spentOn.value }, issues.minOf { it.startDate.value })
        val endDate = maxOf(timeEntries.maxOf { it.spentOn.value }, issues.mapNotNull { it.closeOn?.value }.maxOf { it }, issues.mapNotNull { it.dueDate?.value }.maxOf { it }).plusDays(1)
        val budgetAtCompletion = floor(issues.mapNotNull { it.estimateHours?.value }.sum().times(100F)).div(100F)

        val earnedSchedules = mutableListOf<EarnedSchedule>()

        for(currentDate in beginDate.datesUntil(endDate)){
            // PVを設定
            issues.filter{ it.estimateHours != null && it.plannedValue.value != it.estimateHours.value && currentDate >= it.startDate.value}.forEach{ issue ->
                val onePoint = issue.estimateHours?.value?.times(1F.div((issue.dueDate?.value ?: endDate).compareTo(issue.startDate.value).plus(1) ))
                val diffDate = currentDate.compareTo(issue.startDate.value) + 1
                issue.plannedValue = PlannedValue(if(currentDate < (issue.dueDate?.value ?: endDate)) onePoint?.times(diffDate)?.times(100)?.let { it -> ceil(it) }?.div(100) ?: 0F else issue.estimateHours?.value ?: 0F)
            }

            // EVを設定
            issues.filter{ it.estimateHours != null && it.earnedValue.value != it.estimateHours.value }.forEach{ issue ->
                issue.doneRatio = DoneRatioService.calculate(issues, issue, currentDate)
                issue.earnedValue = EarnedValue(issue.estimateHours?.value?.times(issue.doneRatio.value)?.let { it -> ceil(it)}?.div(100F) ?: 0F)
            }

            // EarnedSchedule追加
            earnedSchedules.add(EarnedSchedule(
                EarnedScheduleDate(currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE)),
                BudgetAtCompletion(budgetAtCompletion),
                PlannedValue(issues.sumOf { it.plannedValue.value.toBigDecimal() }.toFloat()),
                ActualCost(timeEntries.filter{currentDate >= it.spentOn.value}.sumOf { it.hours.value.toBigDecimal() }.toFloat()),
                EarnedValue(issues.sumOf { it.earnedValue.value.toBigDecimal() }.toFloat()),
            ))
        }

        return earnedSchedules
    }
}