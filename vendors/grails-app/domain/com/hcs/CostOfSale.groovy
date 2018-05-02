package com.hcs

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CostOfSale {

    LocalDate dayOfCost
    BigDecimal amount


    static belongsTo = [vendor: Vendor]

    // static embedded = ['dayOfCost', 'amount']

    static constraints = {
        dayOfCost(nullable: false)
        amount(nullable: false)
    }

    String toString() { "CostOfSale \$$amount for $vendor on ${dayOfCost.format(DateTimeFormatter.ofPattern('yyyy-MMM-dd'))}"}
}
