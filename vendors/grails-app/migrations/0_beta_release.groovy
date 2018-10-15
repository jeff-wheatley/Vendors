databaseChangeLog = {

    changeSet(author: "jwheatley (generated)", id: "1524397989017-1") {
        createTable(tableName: "commission") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "commissionPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "day_of_commission", type: "date") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "commission_vendor_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1524397989017-2") {
        createTable(tableName: "commission_vendor") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "commission_vendorPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(40)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1524397989017-3") {
        createTable(tableName: "cost_of_sale") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "cost_of_salePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "vendor_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "day_of_cost", type: "date") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1524397989017-4") {
        createTable(tableName: "operational_expense") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "operational_expensePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "day_of_expense", type: "date") {
                constraints(nullable: "false")
            }

            column(name: "operational_expense_type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(60)")
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1524397989017-5") {
        createTable(tableName: "report_cycle") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "report_cyclePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "cycle", type: "date") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1524397989017-6") {
        createTable(tableName: "sale") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "salePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "day_of_sale", type: "date") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1524397989017-7") {
        createTable(tableName: "vendor") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "vendorPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(40)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1524397989017-8") {
        addForeignKeyConstraint(baseColumnNames: "commission_vendor_id", baseTableName: "commission", constraintName: "FK6c49tt7p4cxhft8xhrmvnlcgp", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "commission_vendor")
    }

    changeSet(author: "jwheatley (generated)", id: "1524397989017-9") {
        addForeignKeyConstraint(baseColumnNames: "vendor_id", baseTableName: "cost_of_sale", constraintName: "FKrm0sby4ss6hevj8as1kjatelt", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "vendor")
    }
}
