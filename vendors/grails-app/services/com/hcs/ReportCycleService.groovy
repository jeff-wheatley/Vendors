package com.hcs

import grails.gorm.services.Service

@Service(ReportCycle)
interface ReportCycleService {

    ReportCycle get(Serializable id)

    List<ReportCycle> list(Map args)

    Long count()

    void delete(Serializable id)

    ReportCycle save(ReportCycle reportCycle)

}
