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
    ReportCycleService reportCycleService
    SummaryService summaryService
    ReportService reportService
    CycleService cycleService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static DATE_FORMAT = "yyyy-MMM-dd"
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT)

    LocalDate parseDate( String date ) {
        LocalDate.parse( date, dateFormat)
    }

    /**
     * Parse the string activity date and validate it is within the valid date range,
     * between the startDate of the currentCycle, and the end date of the current month.
     * @param activityDateString
     * @return the LocalDate representation of the activityDateString, if valid.
     * @post a valid activityDate is set on session.activityDate.
     */
    LocalDate parseAndValidateActivityDate( String activityDateString ) {
        LocalDate activityDate = parseDate( activityDateString )
        ReportCycle currentCycle = CurrentCycle.currentCycle.cycle
        if( activityDate < currentCycle.startDate ) {
            throw new HomeControllerException("Activity date can not be prior to the current cycle (must be on or after ${currentCycle.startDate.format( dateFormat )}).")
        }
        ReportCycle nowCycle = ReportCycle.fromLocalDate( LocalDate.now())
        if( activityDate > nowCycle.endDate ) {
            throw new HomeControllerException( "Activity date cannot be past the end of this month (no later than ${nowCycle.endDate.format( dateFormat )}).")
        }
        session.activityDate = activityDate
        activityDate
    }

    def index(HomeCommand command) {
        command.clearErrors()
        ReportCycle cycle = CurrentCycle.currentCycle.cycle
        command.currentCycle = cycle.toString()
        command.currentCycleClosed = cycle.closed
        command.currentCycleCloseElegible = cycle.closeElegible
        command.nextCycle = cycle.nextCycle.toString()
params.max = 20
        if(params.startDate == null) {
            command.startDate = cycle.startDate
            command.endDate = cycle.endDate
        }
        else {
            command.startDate = new LocalDate( params.startDate_year.toInteger(), params.startDate_month.toInteger(), params.startDate_day.toInteger() )
            command.endDate = new LocalDate( params.endDate_year.toInteger(), params.endDate_month.toInteger(), params.endDate_day.toInteger() )
        }
        command.recentCycles = reportService.recentCycles
        Map dates = summaryService.alignDatesForReportByType( command.startDate, command.endDate, command.reportByType )
        command.startDate = dates.startDate
        command.endDate = dates.endDate
        command.revenueExpenseSummary = summaryService.revenueExpenseSummary(command.startDate, command.endDate, command.reportByType, command.showStartAndEndDatesOnly)
        command.vendorSummary = summaryService.vendorSummary(command.startDate, command.endDate, command.reportByType, command.showStartAndEndDatesOnly)
        command.commissionVendorSummary = summaryService.commissionVendorSummary(command.startDate, command.endDate, command.reportByType, command.showStartAndEndDatesOnly)
        command.operationalExpenseSummary = summaryService.operationalExpenseSummary(command.startDate, command.endDate, command.reportByType, command.showStartAndEndDatesOnly)
        String today = session?.activityDate?.format( dateFormat ) ?: LocalDate.now().format( dateFormat )
        respond command: command, dateFormat: dateFormat, today: today, sale:new Sale(), commission: new Commission(), costOfSale: new CostOfSale(), operationalExpense: new OperationalExpense()
    }

    def cycleChanged( String cycleName) {
        ReportCycle cycle = ReportCycle.fromName( params.cycle )
        render g.field( name:'startDate', type:'date', value:cycle.startDate) + g.field( name:'endDate', type:'date', value:cycle.endDate)
    }

    def updateCycle( HomeCommand command ) {
        System.out.println("advanceCycle start")
        ReportCycle cycle = CurrentCycle.currentCycle.cycle
        try {
            if( !cycle.closed ) {
                cycle = cycleService.closeCycle().cycle
                System.out.println("advanceCycle completed the cycle close")
            } else {
                cycle = cycleService.advanceCycle().cycle
                System.out.println("advanceCycle completed the advance")
            }
        } catch (ValidationException | IllegalArgumentException | DateTimeParseException | CycleServiceException e) {
            System.out.println("advanceCycle exception $e")
            flash.message ="${e.message}"
            params.reportCycleErrors = cycle.errors
            redirect( action:'index' )
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: ["$cycle"])
                redirect(action: 'index')
            }
            '*' { respond command, [status: CREATED] }
        }
        System.out.println("advanceCycle end")
        }

    def saveSale(Sale sale) {
        if (sale == null) {
            notFound()
            return
        }

        try {
            // FIXME this should not be required, but the parse date on Sale blows up if passed the object from GSP land
            sale = new Sale(amount: sale.amount, dayOfSale: parseAndValidateActivityDate(params.dayOfSale))
            saleService.save(sale)
        } catch (ValidationException | IllegalArgumentException | DateTimeParseException | HomeControllerException e) {
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
            commission.dayOfCommission = parseAndValidateActivityDate(params.dayOfCommission)
            commission.errors = null
            commissionService.save(commission)
        } catch (ValidationException | IllegalArgumentException | DateTimeParseException | HomeControllerException e) {
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
        System.out.println("starting saveCostOfSale")
        if (costOfSale == null) {
            notFound()
            return
        }

        try {
            costOfSale.dayOfCost = parseAndValidateActivityDate(params.dayOfCost)
            costOfSale.errors = null
            costOfSaleService.save(costOfSale)
        } catch (ValidationException | IllegalArgumentException | DateTimeParseException | HomeControllerException  e) {
            flash.message ="${e.message}"
            params.costOfSaleErrors = costOfSale.errors
            redirect( action:'index' )
            return
        }
        System.out.println("Completing saveCostOfSale")

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: ["$costOfSale"])
                redirect( action:'index' )
            }
            '*' { respond costOfSale, [status: CREATED] }
        }
        System.out.println("done with saveSale")
    }


    def saveOperationalExpense(OperationalExpense operationalExpense) {
        if (operationalExpense == null) {
            notFound()
            return
        }

        try {
            operationalExpense.dayOfExpense = parseAndValidateActivityDate(params.dayOfExpense)
            operationalExpense.errors = null
            operationalExpenseService.save(operationalExpense)
        } catch (ValidationException | IllegalArgumentException | DateTimeParseException | HomeControllerException e) {
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
    String currentCycle
    Boolean currentCycleClosed = false
    Boolean currentCycleCloseElegible = false
    String nextCycle
    List<ReportCycle> recentCycles
    Map revenueExpenseSummary
    Map commissionVendorSummary
    Map vendorSummary
                               Map operationalExpenseSummary
    }


class HomeControllerException extends RuntimeException {
    HomeControllerException( String msg ) {
        super(msg)
    }
}

