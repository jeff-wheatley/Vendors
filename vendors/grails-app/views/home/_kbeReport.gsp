<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
 <html>
     <head>
         <meta name="layout" content="main" />
         <title>Financial Report; Kentucky Business Enterprises Program</title>
     </head>
     <body>
<div id="summary-period" class="content scaffold-create" role="main">
<h1>Financial Report</h1>
<h1>Kentucky Business Enterprises Program</h1>

<p>Vending Facility: ${vendingFacility}</p>
<p>Manager: ${manager}</p>

<h1>Income Statement for Month Ending: ${endDate}</h1>

<table>
<g:each in="${rows}" var="row">
<tr>
    <td>${row.label}</td>
    <td>${row.subValue1}</td>
    <td>${row.subLabel2}</td>
    <td>${row.subValue2}</td>
    <td>${row.operationLabel}</td>
    <td>${row.value}</td>
  </tr>
</g:each>
</table>
<p></p>
<p>This is to verify that this report is correct to the best of my knowledge.</p>

<p>______________________________________________________________________________</p>
<p>Manager's Signature                      Date</p>

						<p>____________________________________</p>
						<p>Gallons of Coca Cola Syrup Purchased</p>

</div>
</body>
</html>
