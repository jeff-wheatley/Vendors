<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <title><g:message code="vendors.title" /></title>
            <asset:javascript src="application"/>
            <asset:stylesheet src="application"/>
    </head>
    <body>
        <a href="#update-sale-expense" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                            <li><a href="${createLink(uri: '/vendor')}"><g:message code="vendors.vendor.management"/></a></li>
            <li><a href="${createLink(uri: '/CommissionVendor')}"><g:message code="vendors.commission.vendor.management"/></a></li>
                <li><a href="${createLink(uri: '/Commission')}"><g:message code="vendors.commission.management"/></a></li>
                <li><a href="${createLink(uri: '/costOfSale')}"><g:message code="vendors.cost.of.sales.management"/></a></li>
                <li><a href="${createLink(uri: '/operationalExpense')}"><g:message code="vendors.operational.expense.management"/></a></li>
                <li><a href="${createLink(uri: '/sale')}"><g:message code="vendors.sale.management"/></a></li>
            </ul>
        </div>

        <div id="update-sale-costs" class="content scaffold-create" role="main">
        <h1><g:message code="vendors.update.expense.sales.header" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>

        <div id="create-cost" class="content scaffold-create" role="main">
                    <h2><g:message code="vendors.add.cost.of.sale.header" /></h2>
                    <g:hasErrors bean="${params.costOfSaleErrors}">
                                <ul class="errors" role="alert">
                                    <g:eachError bean="${params.costOfSaleErrors}" var="error">
                                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                                    </g:eachError>
                                </ul>
                                </g:hasErrors>
                    <g:form url="[controller:'home', action:'saveCostOfSale']" method="POST">
                        <fieldset class="form">
                        <label for="dayOfCost">Date of Cost *</label>
                        <g:field name="dayOfCost" type="date" value="${today}" />
                        <f:field bean="costOfSale" property="vendor"/>
                        <f:field bean="costOfSale" property="amount"/>
                                                    </fieldset>
                        <fieldset class="buttons">
                            <g:submitButton name="create" class="save" action="saveCostOfSale" value="${message(code: 'vendors.add.cost.of.sale.save')}" />
                        </fieldset>
                    </g:form>
                </div>

            <div id="create-sale" class="content scaffold-create" role="main">
            <h2><g:message code="vendors.add.sale.header" /></h2>
            <g:hasErrors bean="${params.saleErrors}">
            <ul class="errors" role="alert">
                <g:eachError bean="${params.saleError}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form url="[controller:'home', action:'saveSale']" method="POST">
                <fieldset class="form">
                <label for="dayOfSale">Date of Sale *</label>
                    <g:field name="dayOfSale" type="date" value="${today}" />
                                            <f:field bean="sale" property="amount"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" action="saveSale" value="${message(code: 'vendors.add.sale.save')}" />
                </fieldset>
            </g:form>
        </div>

<div id="create-commission" class="content scaffold-create" role="main">
            <h2><g:message code="vendors.add.commission.header" /></h2>
            <g:hasErrors bean="${params.commissionErrors}">
            <ul class="errors" role="alert">
                <g:eachError bean="${params.commissionErrors}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form url="[controller:'home', action:'saveCommission']" method="POST">
                <fieldset class="form">
                <label for="dayOfCommission">Date of Commission *</label>
                    <g:field name="dayOfCommission" type="date" value="${today}" />
                    <f:field bean="commission" property="commissionVendor"/>
                    <f:field bean="commission" property="amount"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" action="saveCommission" value="${message(code: 'vendors.add.commission.save')}" />
                </fieldset>
            </g:form>
        </div>

        <div id="create-operational-expense" class="content scaffold-create" role="main">
                    <h2><g:message code="vendors.add.operational.expense.header" /></h2>
                    <g:hasErrors bean="${params.operationalExpenseErrors}">
                                <ul class="errors" role="alert">
                                    <g:eachError bean="${params.operationalExpenseErrors}" var="error">
                                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                                    </g:eachError>
                                </ul>
                                </g:hasErrors>
                    <g:form url="[controller:'home', action:'saveOperationalExpense']" method="POST">
                        <fieldset class="form">
                        <label for="dayOfExpense">Date of Expense *</label>
                            <g:field name="dayOfExpense" type="date" value="${today}" />
                            <f:field bean="operationalExpense" property="operationalExpenseType"/>
                            <f:field bean="operationalExpense" property="amount"/>
                        </fieldset>
                        <fieldset class="buttons">
                            <g:submitButton name="create" class="save" action="saveOperationalExpense" value="${message(code: 'vendors.add.operational.expense.save')}" />
                        </fieldset>
                    </g:form>
                </div>

        </div>

                </div>

<div id="view-summary" class="content scaffold-create" role="main">
        <h1><g:message code="vendors.financial.summary.header"/></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div id="summary-period" class="content scaffold-create" role="main">
            <h2><g:message code="vendors.confirm.dates.summary.header" /></h2>
            <g:hasErrors bean="${this.command}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.command}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form url="[controller:'home']" method="POST">
                <fieldset class="form">
                    <label for="startDate">Start Date: </label>
                    <g:datePicker name="startDate" precision="day" value="${command.startDate}" relativeYears="[-10..0]" />
                    <label for="endDate">End Date: </label>
                    <g:datePicker name="endDate" precision="day" value="${command.endDate}" relativeYears="[-10..0]" />
                    <f:field bean="command" property="reportByType" />
                    <f:field bean="command" property="showStartAndEndDatesOnly" />
             </fieldset>
                <fieldset class="buttons">
                    <g:actionSubmit value="${message(code: 'vendors.update.summary.period')}" action="index" />
                </fieldset>
            </g:form>
        </div>

            <div id="summary-totals0" class="content scaffold-create" role="main">
            <tmpl:summaryTable header="vendors.totals.summary.header" start="${command.startDate}" end="${command.endDate}" data="${command.revenueExpenseSummary}"/>
            </div>

            <div id="summary-commissions" class="content scaffold-create" role="main">
            <tmpl:summaryTable header="vendors.commissions.summary.header" start="${command.startDate}" end="${command.endDate}" data="${command.commissionVendorSummary}"/>
                        </div>

            <div id="summary-vendors" class="content scaffold-create" role="main">
            <tmpl:summaryTable header="vendors.costs.summary.header" start="${command.startDate}" end="${command.endDate}" data="${command.vendorSummary}"/>
                            </div>

            <div id="summary-operational-types" class="content scaffold-create" role="main">
            <tmpl:summaryTable header="vendors.operational.expense.summary.header" start="${command.startDate}" end="${command.endDate}" data="${command.operationalExpenseSummary}"/>
                        </div>

                        </div>

        </div>

<div id="reports" class="content scaffold-create" role="main">
        <h1><g:message code="vendors.reports.header" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div id="kbe-report" class="content scaffold-create" role="main">
            <h2><g:message code="vendors.kbe.report.header" /></h2>
            <g:hasErrors bean="${this.command}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.command}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form url="[controller:'home']" method="POST">
                <fieldset class="form">
                <label for="command.cycle">Month to Report</label>
                <g:select name="command.cycle" from="${command.recentCycles}" value="${command.recentCycles[0]}"/>
             </fieldset>
                <fieldset class="buttons">
                    <g:actionSubmit value="${message(code: 'vendors.view.kbe.report')}" action="kbeReport" />
                    <g:actionSubmit value="${message(code: 'vendors.pdf.kbe.report')}" action="kbeReportPDF" />
                </fieldset>
            </g:form>
        </div>

        </div>

        </div>

        <script>
            function cycleChanged(cycle) {
                jQuery.ajax({type:'POST',data:'cycle='+cycle, url:'home/cycleChanged',success:function(data,textStatus){jQuery('#subContainer').html(data);},error:function(XMLHttpRequest,textStatus,errorThrown){}});
            }
        </script>

    </body>
</html>
