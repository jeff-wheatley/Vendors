package com.hcs


import grails.core.*
import grails.gorm.services.Service
import java.time.temporal.TemporalAdjusters
import org.joda.time.LocalDate
import org.springframework.beans.factory.annotation.*

class SummaryService {

    GrailsApplication grailsApplication

    // Set default period to the first / last day of the current month
    Map getDefaultPeriod() {
        LocalDate today = LocalDate.now()
        // with(TemporalAdjusters.lastDayOfMonth())

        [startDate: today.withDayOfMonth(1), endDate: today.dayOfMonth().withMaximumValue()]
    }

    protected Map revenueExpenseSummaryValues( LocalDate startDate, LocalDate endDate ) {
        assert startDate && endDate, "revenueExpenseSummary does not support null start or end dates: start=$startDate, end=$endDate"

        // Get costOfSales for the specified date range
        List results = CostOfSale.withCriteria {
            projections {
                sum 'amount'
            }
            between('dayOfCost', startDate, endDate)
        }
        BigDecimal costOfSales = results[0]?:0.00

        // Get the gross sales for the specified date range
        results = Sale.withCriteria {
            projections {
                sum 'amount'
            }
            between('dayOfSale', startDate, endDate)
        }
        BigDecimal grossSales = results[0]?:0.00

        // Get the commission total for the specified date range
        results = Commission.withCriteria {
            projections {
                sum 'amount'
            }
            between('dayOfCommission', startDate, endDate)
        }
        BigDecimal commissions = results[0]?:0.00

        // Get the operationalExpense by type and total for the specified period
        Map operationalExpensesByType = operationalExpenseSummary(startDate, endDate)
        BigDecimal operationalExpenses = 0.00
        operationalExpensesByType.each { key, BigDecimal val ->
            operationalExpenses += val
        }

        // Calculate derived values
        BigDecimal grossProfit = grossSales - costOfSales
        BigDecimal adjustedGrossProfit = grossProfit + commissions // + Direct Competition Income if we collected that...
        BigDecimal netProceeds = adjustedGrossProfit - operationalExpenses
        BigDecimal setasidePercent = grailsApplication.config.getProperty('vendors.setaside.percent', BigDecimal)
        assert setasidePercent, "revenueExpenseSummary requires vendors.setaside.percent be defined in configuration."
        BigDecimal setAside = netProceeds * (setasidePercent ?: 0.07)
        setAside = setAside.setScale(2, BigDecimal.ROUND_HALF_EVEN)
        BigDecimal netIncome = netProceeds - setAside

        // return the resultsin an ordered map
        [
                grossSales: grossSales,
                costOfSales: costOfSales,
                grossProfit: grossProfit,
                otherIncome: commissions,
                directCompetitionIncome: null,
                adjustedGrossProfit: adjustedGrossProfit,
                operationalExpenses: operationalExpenses,
                operationalExpensesByType: operationalExpensesByType,
                netProceeds: netProceeds,
                setAside: setAside,
                netIncome: netIncome,
                operatorSalaries: netIncome,
        ]
    }

    Map revenueExpenseSummary( LocalDate startDate, LocalDate endDate ) {
        Map values = revenueExpenseSummaryValues(startDate, endDate)

        // return the resultsin an ordered map
        [
                'A. GrossSales': values.grossSales,
                'B. Cost Of Sales': values.costOfSales,
                'C. Gross Profit': values.grossProfit,
                'D. Other Income': values.otherIncome,
                'E. Direct Competition Income': values.directCompetitionIncome,
                'F. Adjusted Gross Profit': values.adjustedGrossProfit,
                'G. Operational Expenses': values.operationalExpenses,
                'H. Net Proceeds': values.netProceeds,
                'I. Set-Aside': values.setAside,
                'J. Net Income': values.netIncome,
                'K. Operator Salaries': values.operatorSalaries,
        ]
    }
    Map kbeReport( Map args ) {
        assert args.startDate && args.endDate, "kbeReport does not support null start or end dates: start=${args.startDate}, end=${args.endDate}"
        String manager = grailsApplication.config.getProperty('vendors.manager', String)
        String vendingFacility = grailsApplication.config.getProperty('vendors.vending.facility', String)
        assert manager && vendingFacility, "revenueExpenseSummary requires that both vendors.manager and vendors.vending.facility be defined in configuration."

        // Get calculated summary values
        Map values = revenueExpenseSummaryValues(args.startDate, args.endDate)

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

                [ manager: manager, vendingFacility: vendingFacility, endDate: args.endDate, rows: rows]
    }

    Map vendorSummary( LocalDate startDate, LocalDate endDate ) {
        assert startDate && endDate, "vendorSummary does not support null start or end dates: start=$startDate, end=$endDate"

        // Get the sum of expenses for all vendors with transactions in the specified date range.
        List results = CostOfSale.withCriteria {
            projections {
                groupProperty 'vendor'
                sum 'amount'
            }
            between('dayOfCost', startDate, endDate)
        }

        // Move array results to return map sorted by vendor name
        Map vendorSummaries = [:] as TreeMap
        results.each { row ->
            vendorSummaries[row[0].name] = row[1]
        }
        vendorSummaries
    }

    Map operationalExpenseSummary( LocalDate startDate, LocalDate endDate ) {
        assert startDate && endDate, "operationalExpenseSummary does not support null start or end dates: start=$startDate, end=$endDate"

        // Get the sum of operationalExpenses for all types with transactions in the specified date range.
        List results = OperationalExpense.withCriteria {
            projections {
                groupProperty 'operationalExpenseType'
                sum 'amount'
            }
            between('dayOfExpense', startDate, endDate)
        }

        // Move array results to return map sorted by type
        Map typeSummaries = [:] as TreeMap
        results.each { row ->
            typeSummaries[row[0].name()] = row[1]
        }
        typeSummaries
    }


    Map commissionVendorSummary( LocalDate startDate, LocalDate endDate ) {
        assert startDate && endDate, "commissionVendorSummary does not support null start or end dates: start=$startDate, end=$endDate"

        // Get the sum of expenses for all commissionVendors with transactions in the specified date range.
        List results = Commission.withCriteria {
            projections {
                groupProperty 'commissionVendor'
                sum 'amount'
            }
            between('dayOfCommission', startDate, endDate)
        }

        // Move array results to return map sorted by vendor name
        Map vendorSummaries = [:] as TreeMap
        results.each { row ->
            vendorSummaries[row[0].name] = row[1]
        }
        vendorSummaries
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
