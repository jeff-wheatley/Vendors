package com.hcs

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Commission {

    LocalDate dayOfCommission
    BigDecimal amount


    static belongsTo = [commissionVendor: CommissionVendor]

    // static embedded = ['dayOfCommission', 'amount']

    static constraints = {
        dayOfCommission(nullable: false)
        amount(nullable: false)
    }

    String toString() { "Commission \$$amount for $commissionVendor on ${dayOfCommission.format(DateTimeFormatter.ofPattern('yyyy-MMM-dd'))}"}
}
