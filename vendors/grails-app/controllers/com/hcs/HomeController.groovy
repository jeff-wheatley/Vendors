package com.hcs

import grails.validation.Validateable
import grails.validation.ValidationException

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.LocalDate
import static org.springframework.http.HttpStatus.*

class HomeController {

    SaleService saleService
    CommissionService commissionService
CostOfSaleService costOfSaleService
    OperationalExpenseService operationalExpenseService
    SummaryService summaryService
    ReportService reportService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MMM-dd")

    LocalDate parseDate( String date ) {
        LocalDate.parse( date, dateFormat)
    }

    def index(HomeCommand command) {
        command.clearErrors()
        params.max = 20
        if(params.startDate == null) {
            ReportCycle cycle = ReportCycle.currentCycle
            command.startDate = cycle.startDate
            command.endDate = cycle.endDate
            command.cycle = cycle.toString()
        }
        else {
            command.startDate = new LocalDate( params.startDate_year.toInteger(), params.startDate_month.toInteger(), params.startDate_day.toInteger() )
            command.endDate = new LocalDate( params.endDate_year.toInteger(), params.endDate_month.toInteger(), params.endDate_day.toInteger() )
        }
        command.recentCycles = reportService.recentCycles
        command.revenueExpenseSummary = summaryService.revenueExpenseSummary(command.startDate, command.endDate, command.reportByType, command.showStartAndEndDatesOnly)
        command.vendorSummary = summaryService.vendorSummary(command.startDate, command.endDate, command.reportByType, command.showStartAndEndDatesOnly)
        command.commissionVendorSummary = summaryService.commissionVendorSummary(command.startDate, command.endDate, command.reportByType, command.showStartAndEndDatesOnly)
        command.operationalExpenseSummary = summaryService.operationalExpenseSummary(command.startDate, command.endDate, command.reportByType, command.showStartAndEndDatesOnly)
        String today = LocalDate.now().format( dateFormat )
        respond command: command, today: today, sale:new Sale(), commission: new Commission(), costOfSale: new CostOfSale(), operationalExpense: new OperationalExpense()
    }

    def cycleChanged( String cycleName) {
        ReportCycle cycle = ReportCycle.fromName( params.cycle )
        render g.field( name:'startDate', type:'date', value:cycle.startDate) + g.field( name:'endDate', type:'date', value:cycle.endDate)
    }

    def saveSale(Sale sale) {
        if (sale == null) {
            notFound()
            return
        }

        try {
            sale.dayOfSale = parseDate(params.dayOfSale)
            sale.errors = null
            saleService.save(sale)
        } catch (ValidationException | IllegalArgumentException e) {
            flash.message ="${e.message}"
            params.saleErrors = sale.errors
            redirect( action:'index' )
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: ["$sale"])
                redirect( action:'index' )
            }
            '*' { respond sale, [status: CREATED] }
        }
    }

    def saveCommission(Commission commission) {
        if (commission == null) {
            notFound()
            return
        }

        try {
            commission.dayOfCommission = parseDate(params.dayOfCommission)
            commission.errors = null
            commissionService.save(commission)
        } catch (ValidationException | IllegalArgumentException e) {
            flash.message ="${e.message}"
            params.commissionErrors = commission.errors
            redirect( action:'index' )
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: ["$commission"])
                redirect( action:'index' )
            }
            '*' { respond commission, [status: CREATED] }
        }
    }


    def saveCostOfSale(CostOfSale costOfSale) {
        if (costOfSale == null) {
            notFound()
            return
        }

        try {
            costOfSale.dayOfCost = parseDate(params.dayOfCost)
            costOfSale.errors = null
            costOfSaleService.save(costOfSale)
        } catch (ValidationException | IllegalArgumentException e) {
            flash.message ="${e.message}"
            params.costOfSaleErrors = costOfSale.errors
            redirect( action:'index' )
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: ["$costOfSale"])
                redirect( action:'index' )
            }
            '*' { respond costOfSale, [status: CREATED] }
        }
    }


    def saveOperationalExpense(OperationalExpense operationalExpense) {
        if (operationalExpense == null) {
            notFound()
            return
        }

        try {
            operationalExpense.dayOfExpense = parseDate(params.dayOfExpense)
            operationalExpense.errors = null
            operationalExpenseService.save(operationalExpense)
        } catch (ValidationException | IllegalArgumentException e) {
            flash.message = "${e.message}"
            params.operationalExpenseErrors = operationalExpense.errors
            redirect(action: 'index')
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: ["$operationalExpense"])
                redirect(action: 'index')
            }
            '*' { respond operationalExpense, [status: CREATED] }
        }
    }

        def kbeReport(HomeCommand command) {
            render( view: "_kbeReport", model: reportService.kbeReport(command.cycle))
        }


    def kbeReportPDF(HomeCommand command) {
        renderPdf(filename: 'kbeReport.pdf', template:"kbeReport", model:reportService.kbeReport(command.cycle))
        }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: '${propertyName}.label', default: '${className}'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}

class HomeCommand {
      LocalDate startDate
    LocalDate endDate
    ReportByType reportByType = ReportByType.MONTH
    Boolean showStartAndEndDatesOnly = false
    String cycle
    List<ReportCycle> recentCycles
    Map revenueExpenseSummary
    Map commissionVendorSummary
    Map vendorSummary
                               Map operationalExpenseSummary
    }

