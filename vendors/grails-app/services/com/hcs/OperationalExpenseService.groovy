package com.hcs

import grails.gorm.services.Service

@Service(OperationalExpense)
interface OperationalExpenseService {

    OperationalExpense get(Serializable id)

    List<OperationalExpense> list(Map args)

    Long count()

    void delete(Serializable id)

    OperationalExpense save(OperationalExpense operationalExpense)

}
