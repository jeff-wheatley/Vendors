package com.hcs

enum OperationalExpenseType {
	BOOKKEEPING('Bookkeeping'),
	BUSINESSINC('Business Inc'),
	COMMISSIONS('Commissions'),
	EMPLOYEEWAGES('Emp Wages'),
	EMPLOYEEFRINGES('Emp Fringes'),
	EQUIPMENT('Equipment'),
	JANITOR('Janitor'),
	OTHER('Other'),
	RENT('Rent'),
	STOCKLOSS('Stock Loss'),
	SUPPLIES('Supplies'),
	UTILITIES('Utilities'),


	final String description

	OperationalExpenseType(final String description) {
		this.description = description
	}

	@Override
	String toString() {
		description
	}

}
