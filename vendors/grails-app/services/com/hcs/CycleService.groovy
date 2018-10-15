package com.hcs

import grails.core.*
import grails.validation.ValidationException

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CycleService {

    CurrentCycle closeCycle() {
        ReportCycle cycle = CurrentCycle.currentCycle.cycle
        if( cycle.closed ) {
            throw new CycleServiceException("ReportCycle $cycle already closed on ${cycle.closeTime}" )
        }

        // Insure the current cycle period is over before we allow it to be closed.
        if( !cycle.closeElegible ) {
            throw new CycleServiceException("ReportCycle $cycle cannot be closed until its month is over; ${cycle.endDate.plusDays(1)} or later.")
        }
        cycle.closeTime = LocalDateTime.now()
        cycle.save( flush:true )
        CurrentCycle.currentCycle
    }

    CurrentCycle advanceCycle() {
        CurrentCycle currentCycle = CurrentCycle.currentCycle
        if( !currentCycle.cycle.closed ) {
            throw new CycleServiceException("Current ReportCycle ${currentCycle.cycle} must be closed before it can be advanced to the next cycle.")
        }
        currentCycle.cycle = currentCycle.cycle.nextCycle
        currentCycle.save( flush:true )
        currentCycle
    }

}

class CycleServiceException extends RuntimeException {
    CycleServiceException( String msg ) {
        super(msg)
    }
}
