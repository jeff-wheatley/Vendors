package com.hcs

import grails.core.*
import java.time.LocalDate

class ReportService {
    GrailsApplication grailsApplication
    SummaryService summaryService


    List<ReportCycle> getRecentCycles() {
ReportCycle cycle = ReportCycle.currentCycle
        assert cycle, 'A ReportCycle *must* be set as the *currentCycle* to get recentCycles from the ReportService.'

        // create a list with the current and several previous cycles
        List<ReportCycle> cycles = []
        cycles << cycle
        14.times {
            cycle = cycle.prevCycle
            cycles << cycle
        }

        cycles
    }

    Map kbeReport( String cycleName) {
        assert cycleName, "kbeReport requires a valid cycle"
        ReportCycle cycle = ReportCycle.fromName( cycleName )
        assert cycle, "kbeReport requires a valid cycle ($cycleName)"
        String manager = grailsApplication.config.getProperty('vendors.manager', String)
        String vendingFacility = grailsApplication.config.getProperty('vendors.vending.facility', String)
        assert manager && vendingFacility, "revenueExpenseSummary requires that both vendors.manager and vendors.vending.facility be defined in configuration."

        // Get calculated summary values
        Map values = summaryService.revenueExpenseSummaryValues(cycle.startDate, cycle.endDate)

        // and build the table row beans in a list
        List rows = []
        rows << new ReportRow(label: 'A. GrossSales', value: values.grossSales )
        rows << new ReportRow( label: 'B. Cost Of Sales', value:values.costOfSales)
        rows << new ReportRow( label: 'C. Gross Profit (A MINUS B)', operationLabel: 'Total', value: values.grossProfit )
        rows << new ReportRow( label: 'D. Other Income (Coke/Pepsi Commissions)', operationLabel:'add', value: values.otherIncome )
        rows<< new ReportRow( label:'E. Direct Competition Income', operationLabel:'Add', value:values.directCompetitionIncome)
        rows << new ReportRow(label: 'F. Adjusted Gross Profit (C + D + E)', operationLabel:'Total', value:values.adjustedGrossProfit)

        // Handle operational expenses and it's breakdown
        rows << new ReportRow( label:'G. Operational Expenses (Itemized)', operationLabel:'Less', value:values.operationalExpenses)
        rows << new ReportRow(label:OperationalExpenseType.BOOKKEEPING.description, subValue1: values.operationalExpensesByType[OperationalExpenseType.BOOKKEEPING.name()],
                subLabel2: OperationalExpenseType.JANITOR.description, subValue2:values.operationalExpensesByType[OperationalExpenseType.JANITOR.name()] )
        rows << new ReportRow(label:OperationalExpenseType.BUSINESSINC.description, subValue1: values.operationalExpensesByType[OperationalExpenseType.BUSINESSINC.name()],
                subLabel2: OperationalExpenseType.RENT.description, subValue2:values.operationalExpensesByType[OperationalExpenseType.RENT.name()] )
        rows << new ReportRow(label:OperationalExpenseType.COMMISSIONS.description, subValue1: values.operationalExpensesByType[OperationalExpenseType.COMMISSIONS.name()],
                subLabel2: OperationalExpenseType.STOCKLOSS.description, subValue2:values.operationalExpensesByType[OperationalExpenseType.STOCKLOSS.name()] )
        rows << new ReportRow(label:OperationalExpenseType.EMPLOYEEWAGES.description, subValue1: values.operationalExpensesByType[OperationalExpenseType.EMPLOYEEWAGES.name()],
                subLabel2: OperationalExpenseType.SUPPLIES.description, subValue2:values.operationalExpensesByType[OperationalExpenseType.SUPPLIES.name()] )
        rows << new ReportRow(label:OperationalExpenseType.EMPLOYEEFRINGES.description, subValue1: values.operationalExpensesByType[OperationalExpenseType.EMPLOYEEFRINGES.name()],
                subLabel2: OperationalExpenseType.UTILITIES.description, subValue2:values.operationalExpensesByType[OperationalExpenseType.UTILITIES.name()] )
        rows << new ReportRow(label:OperationalExpenseType.EQUIPMENT.description, subValue1: values.operationalExpensesByType[OperationalExpenseType.EQUIPMENT.name()],
                subLabel2: OperationalExpenseType.OTHER.description, subValue2:values.operationalExpensesByType[OperationalExpenseType.OTHER.name()] )

        rows << new ReportRow( label:'H. Net Proceeds (F-G)', operationLabel:'Total', value: values.netProceeds)
        rows << new ReportRow(label:'I. Set-Aside (7%)', operationLabel:'Less', value:values.setAside)
        rows << new ReportRow(label:'J. Net Income', operationLabel:'Total', value:values.netIncome)
        rows << new ReportRow(label:'K. Operator Salaries', operationLabel:'Total', value:values.operatorSalaries)

        [ manager: manager, vendingFacility: vendingFacility, endDate: cycle.endDate, rows: rows]
    }


}


class ReportRow {
    String label
    BigDecimal subValue1
    String subLabel2
    BigDecimal subValue2
    String operationLabel
    BigDecimal value
}
