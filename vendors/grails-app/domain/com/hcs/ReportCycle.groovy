package com.hcs

import java.time.format.DateTimeFormatter
import java.time.LocalDate

class ReportCycle {
    static monthNames = ['N/A', 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ]
    static DateTimeFormatter cycleDateTimeFormatter = DateTimeFormatter.ofPattern("MMMyyyy")

    LocalDate cycle // last day of monthly cycle
    LocalDate startDate // first day of monthly cycle

    static transients = ['startDate']

    // static embedded = ['cycle', 'startDate']

    static constraints = {
        cycle(nullable: false)
    }

    def setCycle( LocalDate cycle ) {
        this.cycle = cycle.withDayOfMonth( cycle.lengthOfMonth())
    }

    LocalDate getStartDate() {
        if( !startDate) { startDate = cycle.withDayOfMonth(1) }
        startDate
    }

    LocalDate getEndDate() { cycle }

    String toString() { cycle.format( cycleDateTimeFormatter ) }

    ReportCycle getPrevCycle() {
        new ReportCycle( cycle: cycle.minusMonths(1))
    }

    ReportCycle getNextCycle() {
        new ReportCycle( cycle: cycle.plusMonths(1))
    }

    static ReportCycle fromName(String cycleName ) {
        assert cycleName?.length()==7, 'cycleName must be a valid ReportCycle name'
        Integer month = monthNames.indexOf(cycleName.substring(0,3))
        Integer year = cycleName.substring(3).toInteger()
        new ReportCycle (cycle: new LocalDate(year, month, 1 ))
    }

    static ReportCycle getCurrentCycle() { get( 1 ) }
}

