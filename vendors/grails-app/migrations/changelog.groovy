databaseChangeLog = {

    changeSet(author: "jwheatley (generated)", id: "1522683669264-1") {
        createTable(tableName: "COMMISSION") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT(19)") {
                constraints(primaryKey: "true", primaryKeyName: "CONSTRAINT_5")
            }

            column(name: "VERSION", type: "BIGINT(19)") {
                constraints(nullable: "false")
            }

            column(name: "DAY_OF_COMMISSION", type: "VARBINARY(255)") {
                constraints(nullable: "false")
            }

            column(name: "AMOUNT", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "COMMISSION_VENDOR_ID", type: "BIGINT(19)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1522683669264-2") {
        createTable(tableName: "COMMISSION_VENDOR") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT(19)") {
                constraints(primaryKey: "true", primaryKeyName: "CONSTRAINT_C")
            }

            column(name: "VERSION", type: "BIGINT(19)") {
                constraints(nullable: "false")
            }

            column(name: "NAME", type: "VARCHAR(40)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1522683669264-3") {
        createTable(tableName: "COST_OF_SALE") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT(19)") {
                constraints(primaryKey: "true", primaryKeyName: "CONSTRAINT_2")
            }

            column(name: "VERSION", type: "BIGINT(19)") {
                constraints(nullable: "false")
            }

            column(name: "VENDOR_ID", type: "BIGINT(19)") {
                constraints(nullable: "false")
            }

            column(name: "DAY_OF_COST", type: "VARBINARY(255)") {
                constraints(nullable: "false")
            }

            column(name: "AMOUNT", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1522683669264-4") {
        createTable(tableName: "OPERATIONAL_EXPENSE") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT(19)") {
                constraints(primaryKey: "true", primaryKeyName: "CONSTRAINT_6")
            }

            column(name: "VERSION", type: "BIGINT(19)") {
                constraints(nullable: "false")
            }

            column(name: "DAY_OF_EXPENSE", type: "VARBINARY(255)") {
                constraints(nullable: "false")
            }

            column(name: "OPERATIONAL_EXPENSE_TYPE", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "AMOUNT", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "DESCRIPTION", type: "VARCHAR(60)")
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1522683669264-5") {
        createTable(tableName: "SALE") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT(19)") {
                constraints(primaryKey: "true", primaryKeyName: "CONSTRAINT_26")
            }

            column(name: "VERSION", type: "BIGINT(19)") {
                constraints(nullable: "false")
            }

            column(name: "DAY_OF_SALE", type: "VARBINARY(255)") {
                constraints(nullable: "false")
            }

            column(name: "AMOUNT", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1522683669264-6") {
        createTable(tableName: "VENDOR") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT(19)") {
                constraints(primaryKey: "true", primaryKeyName: "CONSTRAINT_9")
            }

            column(name: "VERSION", type: "BIGINT(19)") {
                constraints(nullable: "false")
            }

            column(name: "NAME", type: "VARCHAR(40)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1522683669264-7") {
        createIndex(indexName: "FK6C49TT7P4CXHFT8XHRMVNLCGP_INDEX_5", tableName: "COMMISSION") {
            column(name: "COMMISSION_VENDOR_ID")
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1522683669264-8") {
        createIndex(indexName: "FKRM0SBY4SS6HEVJ8AS1KJATELT_INDEX_2", tableName: "COST_OF_SALE") {
            column(name: "VENDOR_ID")
        }
    }

    changeSet(author: "jwheatley (generated)", id: "1522683669264-9") {
        addForeignKeyConstraint(baseColumnNames: "COMMISSION_VENDOR_ID", baseTableName: "COMMISSION", constraintName: "FK6C49TT7P4CXHFT8XHRMVNLCGP", deferrable: "false", initiallyDeferred: "false", onDelete: "RESTRICT", onUpdate: "RESTRICT", referencedColumnNames: "ID", referencedTableName: "COMMISSION_VENDOR")
    }

    changeSet(author: "jwheatley (generated)", id: "1522683669264-10") {
        addForeignKeyConstraint(baseColumnNames: "VENDOR_ID", baseTableName: "COST_OF_SALE", constraintName: "FKRM0SBY4SS6HEVJ8AS1KJATELT", deferrable: "false", initiallyDeferred: "false", onDelete: "RESTRICT", onUpdate: "RESTRICT", referencedColumnNames: "ID", referencedTableName: "VENDOR")
    }
}
