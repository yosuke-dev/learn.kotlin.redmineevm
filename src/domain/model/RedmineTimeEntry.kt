package learn.kotlin.redmineevm.domain.model

import learn.kotlin.redmineevm.domain.value.ActualCost
import learn.kotlin.redmineevm.domain.value.IssueId
import learn.kotlin.redmineevm.domain.value.SpentOn

data class RedmineTimeEntry(
    val issueId: IssueId,
    val spentOn: SpentOn,
    val hours: ActualCost
)