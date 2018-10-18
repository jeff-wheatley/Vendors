package com.hcs

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Commission {

    LocalDate dayOfCommission
    String day
    String month
    String year
    BigDecimal amount


    static belongsTo = [commissionVendor: CommissionVendor]

    // static embedded = ['dayOfCommission', 'amount']

    static constraints = {
        dayOfCommission(nullable: false)
        amount(nullable: false)
    }


    static mapping = {
        day formula: 'FORMATDATETIME(DAY_OF_COMMISSION, \'yyyy-MMM-dd\')'
        month formula: 'FORMATDATETIME(DAY_OF_COMMISSION, \'yyyy-MMM\')'
        year formula: 'FORMATDATETIME(DAY_OF_COMMISSION, \'yyyy\')'
    }

    String toString() { "Commission \$$amount for $commissionVendor on ${dayOfCommission.format(DateTimeFormatter.ofPattern('yyyy-MMM-dd'))}"}
}
