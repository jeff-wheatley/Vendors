package com.hcs

import java.time.temporal.ChronoUnit
import java.time.Period
import java.time.format.DateTimeFormatter

enum ReportByType {
	DAY(ChronoUnit.DAYS, Period.ofDays(1), DateTimeFormatter.ofPattern("yyyy-MMM-dd")),
	MONTH(ChronoUnit.MONTHS, Period.ofMonths(1), DateTimeFormatter.ofPattern("MMMyyyy")),
	YEAR(ChronoUnit.YEARS, Period.ofYears(1), DateTimeFormatter.ofPattern("yyyy"))

	Period period
	ChronoUnit chronoUnit
	DateTimeFormatter dateTimeFormatter

	ReportByType(ChronoUnit chronoUnit, Period period, DateTimeFormatter dateTimeFormatter) {
		this.chronoUnit = chronoUnit
		this.period = period
		this.dateTimeFormatter = dateTimeFormatter
	}
}
