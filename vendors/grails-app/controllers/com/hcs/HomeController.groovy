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
    static DateTimeFormatter defaultLocalDateFormat = DateTimeFormatter.ofPattern("yyyy-mm-yyyy")

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        HomeCommand command = new HomeCommand(params)
        if(!command.startDate) {
            Map period = summaryService.defaultPeriod
            command.startDate = period.startDate
            command.endDate = period.endDate
        }
        command.recentCycles = reportService.recentCycles
        command.revenueExpenseSummary = summaryService.revenueExpenseSummary(command.startDate, command.endDate)
        command.vendorSummary = summaryService.vendorSummary(command.startDate, command.endDate)
        command.commissionVendorSummary = summaryService.commissionVendorSummary(command.startDate, command.endDate)
        command.operationalExpenseSummary = summaryService.operationalExpenseSummary(command.startDate, command.endDate)
        LocalDate today = LocalDate.now()
        respond command: command, sale:new Sale(dayOfSale:today), commission: new Commission(dayOfCommission:today), costOfSale: new CostOfSale(dayOfCost:today), operationalExpense: new OperationalExpense(dayOfExpense:today)
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
            sale.dayOfSale = LocalDate.parse(params.dayOfSale)
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
            commission.dayOfCommission = LocalDate.parse(params.dayOfCommission)
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
            costOfSale.dayOfCost = LocalDate.parse(params.dayOfCost)
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
            operationalExpense.dayOfExpense = LocalDate.parse(params.dayOfExpense)
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

        def kbeReport(Integer max) {
            // convert params property values of start and end dates from String to LocalDate
            try {
                params.startDate = LocalDate.parse(params.startDate)
                params.endDate = LocalDate.parse(params.endDate)
            } catch( Exception e ) {
                flash.message = "${e.message}"
                redirect(action: 'index')
                return
            }

            render( view: "_kbeReport", model: reportService.kbeReport(params))
        }


    def kbeReportPDF(Integer max) {
        // convert params property values of start and end dates from String to LocalDate
        try {
            params.startDate = LocalDate.parse(params.startDate)
            params.endDate = LocalDate.parse(params.endDate)
        } catch( Exception e ) {
            flash.message = "${e.message}"
            redirect(action: 'index')
            return
        }

        renderPdf(filename: 'kbeReport.pdf', template:"kbeReport", model:reportService.kbeReport(params))
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
    List recentCycles
    Map revenueExpenseSummary
    Map commissionVendorSummary
    Map vendorSummary
                               Map operationalExpenseSummary
    Map stockLossSummary

    HomeCommand(Map args) {
        startDate = args.startDate ? LocalDate.parse(args.startDate ) : null
        endDate = args.endDate ? LocalDate.parse( args.endDate ) : null
    }
    }

