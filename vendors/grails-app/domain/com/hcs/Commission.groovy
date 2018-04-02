package com.hcs

import org.joda.time.LocalDate

class Commission {

    LocalDate dayOfCommission
    BigDecimal amount


    static belongsTo = [commissionVendor: CommissionVendor]

    // static embedded = ['dayOfCommission', 'amount']

    static constraints = {
        dayOfCommission(nullable: false)
        amount(nullable: false)
    }
}
