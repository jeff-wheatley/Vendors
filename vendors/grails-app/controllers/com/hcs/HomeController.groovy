package com.hcs

import grails.validation.Validateable
import grails.validation.ValidationException
import org.joda.time.LocalDate
import static org.springframework.http.HttpStatus.*

class HomeController {

    SaleService saleService
    CommissionService commissionService
CostOfSaleService costOfSaleService
    OperationalExpenseService operationalExpenseService
    SummaryService summaryService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        HomeCommand command = new HomeCommand(params)
        if(!command.startDate) {
            Map period = summaryService.defaultPeriod
            command.startDate = period.startDate
            command.endDate = period.endDate
        }
        command.revenueExpenseSummary = summaryService.revenueExpenseSummary(command.startDate, command.endDate)
        command.vendorSummary = summaryService.vendorSummary(command.startDate, command.endDate)
        command.commissionVendorSummary = summaryService.commissionVendorSummary(command.startDate, command.endDate)
        command.operationalExpenseSummary = summaryService.operationalExpenseSummary(command.startDate, command.endDate)
        LocalDate today = LocalDate.now()
        respond command: command, sale:new Sale(dayOfSale:today), commission: new Commission(dayOfCommission:today), costOfSale: new CostOfSale(dayOfCost:today), operationalExpense: new OperationalExpense(dayOfExpense:today)
    }

    def saveSale(Sale sale) {
        if (sale == null) {
            notFound()
            return
        }

        try {
            saleService.save(sale)
        } catch (ValidationException e) {
            respond sale.errors, view:'index'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'sale.label', default: 'Sale'), sale.id])
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
            commissionService.save(commission)
        } catch (ValidationException e) {
            respond commission.errors, view:'index'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'commission.label', default: 'Commission'), commission.id])
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
            costOfSaleService.save(costOfSale)
        } catch (ValidationException e) {
            log.error("Caught validation error saving costOfSale! ${e.message}")
            respond costOfSale.errors, view:'index'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'costOfSale.label', default: 'CostOfSale'), costOfSale.id])
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
            operationalExpenseService.save(operationalExpense)
        } catch (ValidationException e) {
            respond operationalExpense.errors, view:'index'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'operationalExpense.label', default: 'OperationalExpense'), operationalExpense.id])
                redirect( action:'index' )
            }
            '*' { respond operationalExpense, [status: CREATED] }
        }
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

