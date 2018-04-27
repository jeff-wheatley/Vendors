package com.hcs

import java.time.LocalDate

class Sale {

    LocalDate dayOfSale
    BigDecimal amount

    // static embedded = ['dayOfSale', 'amount']

    static constraints = {
        dayOfSale(nullable: false)
        amount(nullable: false)
    }

    String toString() { "Sale \$$amount on $dayOfSale"}
}
