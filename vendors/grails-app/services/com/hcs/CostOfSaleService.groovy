package com.hcs

import grails.gorm.services.Service

@Service(CostOfSale)
interface CostOfSaleService {

    CostOfSale get(Serializable id)

    List<CostOfSale> list(Map args)

    Long count()

    void delete(Serializable id)

    CostOfSale save(CostOfSale costOfSale)

}
