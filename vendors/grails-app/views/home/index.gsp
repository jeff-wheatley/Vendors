<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <title><g:message code="vendors.title" /></title>
    </head>
    <body>
        <a href="#update-sale-expense" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/home')}"><g:message code="default.home.label"/></a></li>
                <li><a href="${createLink(uri: '/Commission')}"><g:message code="vendors.commission.management"/></a></li>
                <li><a href="${createLink(uri: '/CommissionVendor')}"><g:message code="vendors.commission.vendor.management"/></a></li>
                <li><a href="${createLink(uri: '/costOfSale')}"><g:message code="vendors.cost.of.sales.management"/></a></li>
                <li><a href="${createLink(uri: '/operationalExpense')}"><g:message code="vendors.operational.expense.management"/></a></li>
                <li><a href="${createLink(uri: '/sale')}"><g:message code="vendors.sale.management"/></a></li>
                <li><a href="${createLink(uri: '/vendor')}"><g:message code="vendors.vendor.management"/></a></li>
            </ul>
        </div>

        <div id="update-sale-costs" class="content scaffold-create" role="main">
        <h1>Update Sales and Costs</h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>

        <div id="create-cost" class="content scaffold-create" role="main">
                    <h2>Add Cost of Sale</h2>
                    <g:hasErrors bean="${this.costOfSale}">
                                <ul class="errors" role="alert">
                                    <g:eachError bean="${this.costOfSale}" var="error">
                                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                                    </g:eachError>
                                </ul>
                                </g:hasErrors>
                    <g:form url="[controller:'home', action:'saveCostOfSale']" method="POST">
                        <fieldset class="form">
                            <f:all bean="costOfSale"/>
                        </fieldset>
                        <fieldset class="buttons">
                            <g:submitButton name="create" class="save" action="saveCostOfSale" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                        </fieldset>
                    </g:form>
                </div>

            <div id="create-sale" class="content scaffold-create" role="main">
            <h2>Add Sale</h2>
            <g:hasErrors bean="${this.sale}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.sale}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form url="[controller:'home', action:'saveSale']" method="POST">
                <fieldset class="form">
                    <f:all bean="sale"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" action="saveSale" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>

<div id="create-commission" class="content scaffold-create" role="main">
            <h2>Add Commission</h2>
            <g:hasErrors bean="${this.commission}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.commission}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form url="[controller:'home', action:'saveCommission']" method="POST">
                <fieldset class="form">
                    <f:all bean="commission"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" action="saveCommission" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>

        <div id="create-operational-expense" class="content scaffold-create" role="main">
                    <h2>Add Operational Expense</h2>
                    <g:hasErrors bean="${this.operationalExpense}">
                                <ul class="errors" role="alert">
                                    <g:eachError bean="${this.operationalExpense}" var="error">
                                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                                    </g:eachError>
                                </ul>
                                </g:hasErrors>
                    <g:form url="[controller:'home', action:'saveOperationalExpense']" method="POST">
                        <fieldset class="form">
                            <f:all bean="operationalExpense"/>
                        </fieldset>
                        <fieldset class="buttons">
                            <g:submitButton name="create" class="save" action="saveOperationalExpense" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                        </fieldset>
                    </g:form>
                </div>

        </div>

                </div>

<div id="view-summary" class="content scaffold-create" role="main">
        <h1>Financial Summary</h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div id="summary-period" class="content scaffold-create" role="main">
            <h2>Confirm Dates of Summary</h2>
            <g:hasErrors bean="${this.command}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.command}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form url="[controller:'home', action:'index']" method="POST">
                <fieldset class="form">
                    <f:field bean="command" property="startDate"/>
                    <f:field bean="command" property="endDate"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="search" class="search" action="index" value="${message(code: 'default.button.search.label', default: 'Search')}" />
                </fieldset>
            </g:form>
        </div>

            <div id="summary-totals" class="content scaffold-create" role="main">
                        <h2>Period Summary</h2>
                        <ul class="main" role="main">
                            <g:each in="${this.command.revenueExpenseSummary}" var="item">
                            <li>${item.key} = ${item.value}</li>
                            </g:each>
                        </ul>
                        </div>

            <div id="summary-commissions" class="content scaffold-create" role="main">
                        <h2>Commissions by Vendor</h2>
                        <ul class="main" role="main">
                            <g:each in="${this.command.commissionVendorSummary}" var="item">
                            <li>${item.key} = ${item.value}</li>
                            </g:each>
                        </ul>
                        </div>

            <div id="summary-vendors" class="content scaffold-create" role="main">
                        <h2>Vendor Totals for Period</h2>
                        <ul class="main" role="main">
                            <g:each in="${this.command.vendorSummary}" var="item">
                            <li>${item.key} = ${item.value}</li>
                            </g:each>
                        </ul>
                        </div>

            <div id="summary-operational-types" class="content scaffold-create" role="main">
                        <h2>Operational Totals by Type</h2>
                        <ul class="main" role="main">
                            <g:each in="${this.command.operationalExpenseSummary}" var="item">
                            <li>${item.key} = ${item.value}</li>
                            </g:each>
                        </ul>
                        </div>

                        </div>

        </div>

    </body>
</html>
