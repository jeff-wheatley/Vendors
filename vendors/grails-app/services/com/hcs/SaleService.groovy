package com.hcs

import grails.gorm.services.Service

@Service(Sale)
interface SaleService {

    Sale get(Serializable id)

    List<Sale> list(Map args)

    Long count()

    void delete(Serializable id)

    Sale save(Sale sale)

}
