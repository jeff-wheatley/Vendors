package com.hcs

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OperationalExpense {

    LocalDate dayOfExpense
    String day
    String month
    String year
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

    static mapping = {
        day formula: 'FORMATDATETIME(DAY_OF_EXPENSE, \'yyyy-MMM-dd\')'
        month formula: 'FORMATDATETIME(DAY_OF_EXPENSE, \'yyyy-MMM\')'
        year formula: 'FORMATDATETIME(DAY_OF_EXPENSE, \'yyyy\')'
    }

    String toString() { "OperationalExpense \$$amount for $operationalExpenseType on ${dayOfExpense.format(DateTimeFormatter.ofPattern('yyyy-MMM-dd'))}"}
}
