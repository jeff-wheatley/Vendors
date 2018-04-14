package com.hcs

import org.joda.time.LocalDate

class CostOfSale {

    LocalDate dayOfCost
    BigDecimal amount


    static belongsTo = [vendor: Vendor]

    // static embedded = ['dayOfCost', 'amount']

    static constraints = {
        dayOfCost(nullable: false)
        amount(nullable: false)
    }

    String toString() { "CostOfSale \$$amount for $vendor on $dayOfCost"}
}
