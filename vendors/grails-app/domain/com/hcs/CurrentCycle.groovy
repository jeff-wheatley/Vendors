package com.hcs

import grails.validation.ValidationException

import java.time.LocalDate
import java.time.LocalDateTime

class CurrentCycle {

    ReportCycle cycle

    static constraints = {
        cycle(nullable: false)
    }

    String toString() { "CurrentCycle $cycle" }

    static CurrentCycle getCurrentCycle() { get( 1 ) }

}
