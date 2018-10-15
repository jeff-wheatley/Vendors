package com.hcs

import grails.gorm.services.Service

@Service(CurrentCycle)
interface CurrentCycleService {

    CurrentCycle get(Serializable id)

    List<CurrentCycle> list(Map args)

    Long count()

    void delete(Serializable id)

    CurrentCycle save(CurrentCycle currentCycle)

}
