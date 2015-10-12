<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
    
    <hgroup class="mb20">
      <h1>Auto-Check Result<c:if test="${not empty application}"> for <a href="<c:url value="/applications/${application._appId}" />">${application.fullName}</a></c:if></h1>
      <c:choose>
        <c:when test="${not empty errorMsg}" >
          <h2 class="lead error-msg">${errorMsg}</h2>
        </c:when>
        <c:when test="${not empty successMsg}" >
          <h2 class="lead success-msg">${successMsg}</h2>
        </c:when>
        <c:otherwise>
          <c:if test="${empty autoCheckResult}">
            <h2 class="lead">No result</h2>
          </c:if>
        </c:otherwise>
      </c:choose>
    </hgroup>
    
    <table class="table table-striped">
      <tr>
        <td width="25%">PDV Result</td>
        <td><c:out value="${autoCheckResult.pdvResult}"/></td>
      </tr>
      <tr>
        <td>CRV Result</td>
        <td><c:out value="${autoCheckResult.crvResult}"/></td>
      </tr>
    </table>
    <div class="row">
      <a href="<c:url value="/jobs/${application._jobId}/applications" />">
        <button class="btn btn-default">Back to list</button>
      </a>
    </div>
  </div>
  <%@ include file="footer.html" %>
</body>
</html>