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
        </c:otherwise>
      </c:choose>
    </hgroup>
    
    <table class="table table-striped">
      <tr>
        <td width="25%">PDV Result</td>
        <td><c:out value="${pdvResult}"/></td>
      </tr>
      <tr>
        <td>CRV Result</td>
        <td><c:out value="${crvResult}"/></td>
      </tr>
    </table>
  </div>
  <%@ include file="footer.html" %>
</body>
</html>