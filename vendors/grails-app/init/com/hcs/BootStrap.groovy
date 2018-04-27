package com.hcs

import java.time.LocalDate
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->
        LocalDate date = LocalDate.now()

        // insure we have a current ReportCycle singleton
        ReportCycle currentCycle = ReportCycle.get(1)
        if( !currentCycle ) {
            new ReportCycle( cycle: date).save(flush: true)
        }

        if(Environment.current == Environment.DEVELOPMENT) {


            // Create some reference vendors and commissionVendors
            def sams = new Vendor(name: 'Sams').save(flush: true)
            def cosco = new Vendor(name: 'Cosco').save(flush: true)
            def pepsi = new CommissionVendor(name: 'Pepsi').save(flush: true)
            def coke = new CommissionVendor(name: 'Coke').save(flush: true)

            // create some expenses and operationalExpenses
            new CostOfSale(dateOfCosts: date, amount: 123.45, vendor: sams).save(flush: true)
            new CostOfSale(dateOfCost: date, amount: 242.88, vendor: cosco).save(flush: true)
            new OperationalExpense(dateOfExpense: date, amount: 123.45, type: OperationalExpenseType.JANITOR).save(flush: true)
            new OperationalExpense(dateOfExpense: date, amount: 223.99, type: OperationalExpenseType.UTILITIES).save(flush: true)

            // Create some sales and Commissions
            new Sale(dateOfSale: date, amount: 123.45).save(flush: true)
            new Sale(dateOfSale: date, amount: 255.99).save(flush: true)
            new Commission(dateOfCommission: date, amount: 123.45, commissionVendor: pepsi).save(flush: true)
            new Commission(dateOfCommission: date, amount: 151.50, commissionVendor: coke).save(flush: true)
        }

    }

    def destroy = {
    }
}
