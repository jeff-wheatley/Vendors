package com.hcs


import grails.gorm.services.Service
import java.time.temporal.TemporalAdjusters
import org.joda.time.LocalDate


class SummaryService {

    // Set default period to the first / last day of the current month
    Map getDefaultPeriod() {
        LocalDate today = LocalDate.now()
        // with(TemporalAdjusters.lastDayOfMonth())
        [startDate: today.withDayOfMonth(1), endDate: today.withDayOfMonth(30)] // FIXME
    }

    List salesByPeriod( LocalDate startDate, LocalDate endDate ) {
        assert startDate && endDate, "salesByPeriod does not support null start or end dates: start=$startDate, end=$endDate"
        Sale.withCriteria {
            between('dayOfSale', startDate, endDate)
        }.list()
    }

    Map revenueExpenseSummary( LocalDate startDate, LocalDate endDate ) {
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

        // Get the operationalExpense total for the specified date range
        results = OperationalExpense.withCriteria {
            projections {
                sum 'amount'
            }
            between('dayOfExpense', startDate, endDate)
        }
        BigDecimal operatingExpenses = results[0]?:0.00

        // Calculate derived values
        BigDecimal grossProfit = grossSales - costOfSales
        BigDecimal adjustedGrossProfit = grossProfit + commissions // + Direct Competition Income if we collected that...
        BigDecimal netProceeds = adjustedGrossProfit - operatingExpenses
        BigDecimal setAside =netProceeds * 0.07
        BigDecimal netIncome = netProceeds - setAside

        // return the resultsin an ordered map
        [
                'A. GrossSales': grossSales,
                'B. Cost Of Sales': costOfSales,
                'C. Gross Profit': grossProfit,
                'D. Other Income': commissions,
                'E. Direct Competition Income': '',
                'F. Adjusted Gross Profit': adjustedGrossProfit,
                'G. Operating Expenses': operatingExpenses,
                'H. Net Proceeds': netProceeds,
                'I. Set-Aside': setAside,
                'J. Net Income': netIncome,
                'K. Operator Salaries': '',
        ]
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
