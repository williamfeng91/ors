<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
    
    <hgroup class="mb20">
      <h1>Final List for ${job.positionType}</h1>
      <c:choose>
        <c:when test="${not empty errorMsg}" >
          <h2 class="lead error-msg">${errorMsg}</h2>
        </c:when>
        <c:when test="${not empty successMsg}" >
          <h2 class="lead success-msg">${successMsg}</h2>
        </c:when>
        <c:otherwise>
        </c:otherwise>
      </c:choose>
    </hgroup>

    <table class="table table-striped">
      <tr>
        <td width="50%">Candidate</td>
        <td>Accepted Invitation</td>
      </tr>
      <c:forEach var="application" items="${applications}">
        <tr>
          <td><a href="<c:url value="/applications/${application._appId}" />"><c:out value="${application.name}"/></a></td>
          <td>
            <input type="checkbox" checked disabled />
          </td>
        </tr>
      </c:forEach>
    </table>
  </div>
<%@ include file="footer.html" %>
</body>
</html>