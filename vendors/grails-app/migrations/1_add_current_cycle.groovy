databaseChangeLog = {

    changeSet(author: "jwheatley (generated)", id: "add-current-cycle") {
        createTable(tableName: "current_cycle") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "current_cyclePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "cycle_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "add-close-time-to-report-cycle") {
        addColumn(tableName: "Report_cycle") {
            column(name: "close_time", type: "TIMESTAMP") {
                constraints(nullable: "true")
            }
        }
    }

    changeSet(author: "jwheatley (generated)", id: "foreign-key-constraint-current-cycle-to-report-cycle") {
        addForeignKeyConstraint(baseColumnNames: "cycle_id", baseTableName: "current_cycle", constraintName: "FKfmohj65sl7o8n7kslfnotho46", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "report_cycle")
    }

}
