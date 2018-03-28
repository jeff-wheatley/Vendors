databaseChangeLog = {

    changeSet(author: "jwheatley (generated)", id: "1522172260692-1") {
        createTable(tableName: "expense") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "expensePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "day_of_expense", type: "BLOB(255)") {
                constraints(nullable: "false")
            }

            column(name: "vendor_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1522172260692-2") {
        createTable(tableName: "sale") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "salePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "day_of_sale", type: "BLOB(255)") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1522172260692-3") {
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

    changeSet(author: "jwheatley (generated)", id: "1522172260692-4") {
        addForeignKeyConstraint(baseColumnNames: "vendor_id", baseTableName: "expense", constraintName: "FK5sibxu7jhjrfujed5s55cqfwd", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "vendor")
    }
}
