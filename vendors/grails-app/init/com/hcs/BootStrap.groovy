package com.hcs

import org.joda.time.LocalDate

class BootStrap {

    def init = { servletContext ->
        def lows = new Vendor(name: 'Lowes').save()
        def cosco = new Vendor(name: 'Cosco').save()

        new Expense(day: LocalDate.now(), amount:95.13, vendor: lows).save()
        new Expense(day: LocalDate.now(), amount:99.86, vendor: cosco).save()
        new Sale(day: LocalDate.now(), amount:215.17).save()
    }
    def destroy = {
    }
}
