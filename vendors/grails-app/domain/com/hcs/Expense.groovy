package com.hcs

import org.joda.time.LocalDate

class Expense {

    LocalDate dayOfExpense
    BigDecimal amount


    static belongsTo = [vendor: Vendor]

    static embedded = ['dayOfExpense', 'amount']

    static constraints = {
        dayOfExpense(nullable: false)
        amount(nullable: false)
    }
}
