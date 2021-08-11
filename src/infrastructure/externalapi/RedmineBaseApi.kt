package learn.kotlin.redmineevm.infrastructure.externalapi

import com.taskadapter.redmineapi.RedmineManager
import com.taskadapter.redmineapi.RedmineManagerFactory

open class RedmineBaseApi(uri: String, apikey: String) {
    var mgr: RedmineManager = RedmineManagerFactory.createWithApiKey(uri, apikey)

    init{
        mgr.setObjectsPerPage(100)
    }
}