package com.hcs

import org.joda.time.LocalDate

class OperationalExpense {

    LocalDate dayOfExpense
    BigDecimal amount
OperationalExpenseType operationalExpenseType
    String description

    // static embedded = ['dayOfExpense', 'amount']

    static constraints = {
        dayOfExpense(nullable: false)
        amount(nullable: false)
operationalExpenseType( nullable: false )
        description( nullable: true, maxSize: 60 )
    }

    String toString() { "OperationalExpense \$$amount for $operationalExpenseType on $dayOfExpense"}
}
