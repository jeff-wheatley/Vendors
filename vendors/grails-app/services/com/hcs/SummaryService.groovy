package com.hcs


import grails.core.*
import grails.gorm.services.Service
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.Period
import org.springframework.beans.factory.annotation.*

class SummaryService {

    GrailsApplication grailsApplication

    // For a single period, returns a result Map with summary: Map of relevant KBE metric KEY Value pairs, and operationalExpensesByType: Map<String:BigDecimal> breakdown of the operationalExpense types.
    Map revenueExpenseSummaryValues( LocalDate startDate, LocalDate endDate, ReportByType reportByType) {
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
        Map operationalExpensesResults = operationalExpenseSummary(startDate, endDate, reportByType, false).rows
        Map operationalExpensesByType = [:]
        BigDecimal operationalExpenses = 0.00
        operationalExpensesResults.each { String key, List val ->
            operationalExpenses += val[0]
            operationalExpensesByType << [ (key) : val[0] ]
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
        Map summary = [
                grossSales: grossSales,
                costOfSales: costOfSales,
                grossProfit: grossProfit,
                otherIncome: commissions,
                directCompetitionIncome: 0.00,
                adjustedGrossProfit: adjustedGrossProfit,
                operationalExpenses: operationalExpenses,
                operationalExpensesByType: operationalExpensesByType,
                netProceeds: netProceeds,
                setAside: setAside,
                netIncome: netIncome,
                operatorSalaries: netIncome,
        ] as TreeMap
        [ summary: summary, operationalExpensesByType: operationalExpensesByType ]
    }

    Map alignDatesForReportByType( LocalDate startDate, LocalDate endDate, ReportByType reportByType ) {
        Map dates = [startDate: startDate, endDate: endDate]
        switch( reportByType ) {
            case ReportByType.DAY: break; // dates already correct
            case ReportByType.MONTH: dates.startDate = dates.startDate.withDayOfMonth(1); dates.endDate = dates.endDate.withDayOfMonth( dates.endDate.lengthOfMonth()); break;
            case ReportByType.YEAR: dates.startDate = dates.startDate.withDayOfYear(1); dates.endDate = dates.endDate.withDayOfYear( dates.endDate.lengthOfYear()); break;
            default: assert false, "ReportByType ($reportByType) is not supported."
        }

        dates
    }

    // Determine and create the column headers to be displayed for a summary report
    List<String> columnHeadersForSummary( LocalDate startDate, LocalDate endDate, ReportByType reportByType, Boolean includeStartAndEndDatesOnly  ) {
        assert startDate && endDate && reportByType, "startDate, endDate and reportByType values may not be null ($startDate, $endDate, $reportByType)"

        // Align start and end dates to the reportByType (e.g. insure a month starts on the first and ends on the last day of the relevant month).
        LocalDate start
        LocalDate end
        switch( reportByType ) {
            case ReportByType.DAY: start = startDate; end = endDate; break;
            case ReportByType.MONTH: start = startDate.withDayOfMonth(1); end = endDate.withDayOfMonth( 1 ); break;
            case ReportByType.YEAR: start = startDate.withDayOfYear(1); end = endDate.withDayOfYear( 1 ); break;
            default: assert false, "ReportByType ($reportByType) is not supported."
        }

        // Determine how many periods span start and end?
        Period span = Period.between(startDate, endDate.plusDays(1))
        Period reportPeriod = reportByType.period
        Integer periodCount = 1
        while( start + span > start + reportPeriod) {
            periodCount ++
            span -= reportPeriod
        }

        // We have at least the first period to include
        List<ColumnHeader> columnHeaders = []
        columnHeaders << new ColumnHeader( start, reportByType )
        periodCount--

        // Do we exclude the periods between the start and end periods?
        if( includeStartAndEndDatesOnly && periodCount ) {
            // We do have a distinct start and end period, but we will not show middle periods.
            columnHeaders << new ColumnHeader( end, reportByType )
        }
        else { // show the middle and end columns, if they exist
            while ( start < end ) {
                start += reportPeriod
                columnHeaders << new ColumnHeader( start, reportByType )
            }
            }

columnHeaders
    }

    // Get the specified column's values from columnValues
    List<BigDecimal> getColumnsValues( String columnName, List<Map> columnValues ) {
        List<BigDecimal> values = []
        columnValues.each { Map column ->
            values << column[columnName]
        }
        values
    }

    // return [collumnHeaders:List<String>, rows: [<rowKey> : List<BigDecimal>]] representing the table data for 1 or more periods of revenue expense
    Map revenueExpenseSummary( LocalDate startDate, LocalDate endDate, ReportByType reportByType, Boolean includeStartAndEndDatesOnly  ) {
        // How many periods (sets of values) are specified? (these will be our data columns)
        List<ColumnHeader> columnHeaders = columnHeadersForSummary( startDate, endDate, reportByType, includeStartAndEndDatesOnly)

        // Get the period / column values for each specified period.
        List<Map> columnValues = []
        columnHeaders.each { ColumnHeader columnHeader ->
            columnValues << revenueExpenseSummaryValues(columnHeader.startDate, columnHeader.endDate, reportByType).summary
        }

        // return the resultsin an ordered map
        Map rows = [:] as TreeMap
                rows << ['A. GrossSales': getColumnsValues('grossSales', columnValues ) ]
                rows << ['B. Cost Of Sales': getColumnsValues('costOfSales', columnValues )]
                rows << ['C. Gross Profit': getColumnsValues('grossProfit', columnValues )]
                rows << ['D. Other Income': getColumnsValues('otherIncome', columnValues )]
                rows << ['E. Direct Competition Income': getColumnsValues('directCompetitionIncome', columnValues )]
                rows << ['F. Adjusted Gross Profit': getColumnsValues('adjustedGrossProfit', columnValues )]
                rows << ['G. Operational Expenses': getColumnsValues('operationalExpenses', columnValues )]
                rows << ['H. Net Proceeds': getColumnsValues('netProceeds', columnValues )]
                rows << ['I. Set-Aside': getColumnsValues('setAside', columnValues )]
                rows << ['J. Net Income': getColumnsValues('netIncome', columnValues )]
                rows << ['K. Operator Salaries': getColumnsValues('operatorSalaries', columnValues )]

        [ rows: rows, columnHeaders: columnHeaders]
    }

    // Assumes the query closure returns a projection of String key BigDecimal value pairs, and adds the values to the existing List mapped to key.
    void mapProjectedValues( Map map, LocalDate startDate, LocalDate endDate, Closure query ) {
        assert startDate && endDate, "start and end dates must be provided: start=$startDate, end=$endDate"
        // Run query and move array values to map by key
        query( startDate, endDate ).each { row ->
            map[row[0].name] << row[1]
        }
    }

    // return [collumnHeaders:List<String>, rows: [<rowKey>:List<BigDecimal>]] representing the table data
    Map getSummaryData( LocalDate startDate, LocalDate endDate, ReportByType reportByType, Boolean includeStartAndEndDatesOnly, Closure query ) {
        // How many periods (sets of values) are specified? (these will be our data columns)
        List<ColumnHeader> columnHeaders = columnHeadersForSummary( startDate, endDate, reportByType, includeStartAndEndDatesOnly)

        // Get the period / column values for each specified period and accumulate to rows.
        Map rows = ([:] as TreeMap).withDefault { key -> [] }
        columnHeaders.each { ColumnHeader columnHeader ->
            mapProjectedValues( rows, columnHeader.startDate, columnHeader.endDate, query  )
        }

        [rows: rows, columnHeaders: columnHeaders]
    }

    // return [collumnHeaders:List<String>, rows: [<rowKey>:List<BigDecimal>]] representing the table data
    Map vendorSummary( LocalDate startDate, LocalDate endDate, ReportByType reportByType, Boolean includeStartAndEndDatesOnly   ) {
        // Get the sum of expenses for all vendors with transactions in the specified date range.
        getSummaryData( startDate, endDate, reportByType, includeStartAndEndDatesOnly, { LocalDate start, LocalDate end ->
            CostOfSale.withCriteria {
                projections {
                    groupProperty 'vendor'
                    /* switch(reportByType) {
                        case ReportByType.DAY: groupProperty 'day'; break
                        case ReportByType.MONTH: groupProperty 'month'; break
                        case ReportByType.YEAR: groupProperty 'year'; break
                    }
                    */

                    sum 'amount'
                }
                between('dayOfCost', start, end)
            }
        })
    }

    // return [collumnHeaders:List<String>, rows: [<rowKey>:List<BigDecimal>]] representing the table data
    Map operationalExpenseSummary( LocalDate startDate, LocalDate endDate, ReportByType reportByType, Boolean includeStartAndEndDatesOnly   ) {
        // Get the sum of operationalExpenses for all types with transactions in the specified date range.
        getSummaryData(startDate, endDate, reportByType, includeStartAndEndDatesOnly, { LocalDate start, LocalDate end ->
            OperationalExpense.withCriteria {
                projections {
                    groupProperty 'operationalExpenseType'
                    /* switch(reportByType) {
                        case ReportByType.DAY: groupProperty 'day'; break
                        case ReportByType.MONTH: groupProperty 'month'; break
                        case ReportByType.YEAR: groupProperty 'year'; break
                    }
                    */
                    sum 'amount'
                }
                between('dayOfExpense', start, end)
            }
        })
    }


    // return [collumnHeaders:List<String>, rows: [<rowKey>:List<BigDecimal>]] representing the table data
    Map commissionVendorSummary( LocalDate startDate, LocalDate endDate, ReportByType reportByType, Boolean includeStartAndEndDatesOnly   ) {
        // Get the sum of expenses for all commissionVendors with transactions in the specified date range.
        getSummaryData( startDate, endDate, reportByType, includeStartAndEndDatesOnly, { LocalDate start, LocalDate end ->
            Commission.withCriteria {
                projections {
                    groupProperty 'commissionVendor'
                    /* switch(reportByType) {
                        case ReportByType.DAY: groupProperty 'day'; break
                        case ReportByType.MONTH: groupProperty 'month'; break
                        case ReportByType.YEAR: groupProperty 'year'; break
                    }
                    */

                    sum 'amount'
                }
                between('dayOfCommission', start, end)
            }
        })
    }

    }

class ColumnHeader {
LocalDate startDate
    LocalDate endDate
    String columnName

    // Assumes startDate is set to the first day of the reportByType period (e.g. first day of MONTH or YEAR).
    ColumnHeader( LocalDate startDate, ReportByType reportByType ) {
        this.startDate = startDate

        // Setting endDate is a bit messy, I have not found a more elegant way
        switch( reportByType ) {
            case ReportByType.DAY: endDate = startDate; break;
            case ReportByType.MONTH: endDate = startDate.withDayOfMonth( startDate.lengthOfMonth()); break;
            case ReportByType.YEAR: endDate = startDate.withDayOfYear( startDate.lengthOfYear()); break;
            default: assert false, "ReportByType not supported ($reportByType)"
        }

        columnName = this.endDate.format( reportByType.dateTimeFormatter )
    }

    String toString() { columnName }
}
