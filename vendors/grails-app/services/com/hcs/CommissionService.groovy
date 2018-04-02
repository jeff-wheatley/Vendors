package com.hcs

import grails.gorm.services.Service

@Service(Commission)
interface CommissionService {

    Commission get(Serializable id)

    List<Commission> list(Map args)

    Long count()

    void delete(Serializable id)

    Commission save(Commission commission)

}
