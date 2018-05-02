package com.hcs

import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    String toString() { "OperationalExpense \$$amount for $operationalExpenseType on ${dayOfExpense.format(DateTimeFormatter.ofPattern('yyyy-MMM-dd'))}"}
}
