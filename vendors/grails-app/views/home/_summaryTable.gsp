                      <%@ page import="java.time.format.DateTimeFormatter" %>
                      <h2><g:message code="${header}" /></h2>
                                              <h3>From ${start.format(DateTimeFormatter.ofPattern("yyyy-MMM-dd"))}</h3>
                                              <h3>Through ${end.format(DateTimeFormatter.ofPattern("yyyy-MMM-dd"))}</h3>

<table>
<tr>
<th/>
<g:each in="${data.columnHeaders}" var="header">
<th>${header}</th>
</tr>
</g:each>

<g:each in="${data.rows}" var="row">
<tr>
    <td>${row.key}</td>
    <g:each in="${row.value}" var="cell">
    <td>${cell}</td>
    </g:each>
  </tr>
</g:each>
</table>
