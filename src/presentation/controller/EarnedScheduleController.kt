package learn.kotlin.redmineevm.presentation.controller

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import learn.kotlin.redmineevm.application.service.EarnedScheduleManagement
import learn.kotlin.redmineevm.infrastructure.externalapi.RedmineIssueApi
import learn.kotlin.redmineevm.infrastructure.externalapi.RedmineTimeEntryApi
import learn.kotlin.redmineevm.presentation.request.EarnedScheduleRequest
import learn.kotlin.redmineevm.presentation.response.EarnedScheduleResponse

internal fun Routing.earnedSchedule() {
    route("/evm"){
        post("/version") {
            val input = call.receive<EarnedScheduleRequest>()
            val service = EarnedScheduleManagement(RedmineIssueApi(input.uri, input.apikey), RedmineTimeEntryApi(input.uri, input.apikey))
            val schedules = service.getEarnedValueSchedules(input.versionId).map {
                EarnedScheduleResponse(
                    it.earnedScheduleDate.value,
                    it.budgetAtCompletion.value,
                    it.plannedValue.value,
                    it.actualCost.value,
                    it.earnedValue.value
                )
            }
            call.respond( schedules )
        }
    }
}