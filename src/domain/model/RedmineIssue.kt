package learn.kotlin.redmineevm.domain.model

import learn.kotlin.redmineevm.domain.value.*

data class RedmineIssue(
    val id: IssueId,
    val parent: IssueId?,
    val startDate: StartDate,
    val dueDate: DueDate?,
    val closeOn: CloseOn?,
    val estimateHours: EstimatedHour?,
    val journals: List<RedmineJournal>,
    var doneRatio: DoneRatio = DoneRatio(0),
    var plannedValue: PlannedValue = PlannedValue(0F),
    var earnedValue: EarnedValue = EarnedValue(0F),
)
