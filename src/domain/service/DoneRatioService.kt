package learn.kotlin.redmineevm.domain.service

import learn.kotlin.redmineevm.domain.model.RedmineChildIssue
import learn.kotlin.redmineevm.domain.model.RedmineIssue
import learn.kotlin.redmineevm.domain.value.DoneRatio
import learn.kotlin.redmineevm.domain.value.EarnedValue
import learn.kotlin.redmineevm.domain.value.EstimatedHour
import learn.kotlin.redmineevm.domain.value.IssueId
import java.time.LocalDate
import kotlin.math.ceil

class DoneRatioService {
    companion object
    {
        fun calculate(issues: List<RedmineIssue>, target: RedmineIssue, currentDate: LocalDate): DoneRatio {
            // 子チケットがある場合は、子チケットの見積工数と進捗価値(EV)から計算
            // 子チケットがない場合は、Journalの進捗更新から設定
            return if (hasChild(issues, target.id)) {
                val children: List<RedmineChildIssue> = issues.filter { it.parent == target.id }.map { issue ->
                    val childDoneRatio = calculate(issues, issue, currentDate)
                    RedmineChildIssue(childDoneRatio, issue.estimateHours)
                }
                // estimateHours is not null の平均値をとる。ゼロ件の場合はデフォルト値で計算
                val averageEstimateHour =
                    if (children.any { it.estimateHour != null })
                        children.filter { it.estimateHour != null }.map { it.estimateHour?.value?.toDouble()!! }.average()
                    else
                        EstimatedHour.DEFAULT_VALUE

                // estimateHours is null の estimateHours に 平均値で埋め、earnedValue を 計算
                children.forEach { child ->
                    child.estimateHour = child.estimateHour ?: EstimatedHour(averageEstimateHour.toFloat())
                    child.earnedValue = EarnedValue(child.estimateHour?.value?.times(child.doneRatio.value)?.div(100F) ?: 0F)
                }

                // 合計値を計算
                val sumEstimateHours = children.sumOf { it.estimateHour?.value?.toDouble()!! }
                val sumEarnedValue = children.sumOf { it.earnedValue.value.toDouble() }

                // doneRatioを計算し返す
                DoneRatio(if (sumEstimateHours.equals(0.toDouble())) 0 else ceil(sumEarnedValue.div(sumEstimateHours).times(100F)).toInt())
            } else {
                // doneRatioの計算が不要な場合はそのまま返す
                if (target.doneRatio.isMax()) {
                    target.doneRatio
                } else {
                    // doneRatioの計算はJournalの進捗更新から設定
                    // 対象日以前に進捗登録がない場合はゼロを、進捗登録がある場合は最新日の最大進捗を設定
                    val targetJournals = target.journals.filter { currentDate >= it.createOn.value }
                    if (targetJournals.isNotEmpty()) {
                        val maxDoneRatio = targetJournals
                            .filter { journal -> journal.createOn.value == targetJournals.maxOf { it.createOn.value } }
                            .maxOf { it.doneRatio.value }
                        DoneRatio(maxDoneRatio)
                    } else {
                        DoneRatio(0)
                    }
                }
            }
        }

        private fun hasChild(issues :List<RedmineIssue>, issueId: IssueId): Boolean {
            return issues.any { it.parent == issueId }
        }
    }
}