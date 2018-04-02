package com.hcs

import grails.gorm.services.Service

@Service(CommissionVendor)
interface CommissionVendorService {

    CommissionVendor get(Serializable id)

    List<CommissionVendor> list(Map args)

    Long count()

    void delete(Serializable id)

    CommissionVendor save(CommissionVendor commissionVendor)

}
