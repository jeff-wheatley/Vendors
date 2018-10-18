package com.hcs

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CostOfSale {

    LocalDate dayOfCost
    String day
    String month
    String year
    BigDecimal amount


    static belongsTo = [vendor: Vendor]

    // static embedded = ['dayOfCost', 'amount']

    static constraints = {
        dayOfCost(nullable: false)
        amount(nullable: false)
    }

    static mapping = {
        day formula: 'FORMATDATETIME(DAY_OF_COST, \'yyyy-MMM-dd\')'
        month formula: 'FORMATDATETIME(DAY_OF_COST, \'yyyy-MMM\')'
        year formula: 'FORMATDATETIME(DAY_OF_COST, \'yyyy\')'
    }

    String toString() { "CostOfSale \$$amount for $vendor on ${dayOfCost.format(DateTimeFormatter.ofPattern('yyyy-MMM-dd'))}"}
}
