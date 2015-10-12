<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
    
    <hgroup class="mb20">
      <h1>Reviews<c:if test="${not empty application}"> for <a href="<c:url value="/applications/${application._appId}" />">${application.fullName}</a></c:if></h1>
      <c:choose>
        <c:when test="${not empty errorMsg}" >
          <h2 class="lead error-msg">${errorMsg}</h2>
        </c:when>
        <c:when test="${not empty successMsg}" >
          <h2 class="lead success-msg">${successMsg}</h2>
        </c:when>
      </c:choose>
      <c:if test="${empty reviews}">
        <h2 class="lead">No reviews</h2>
      </c:if>
    </hgroup>
    
    <c:forEach var="review" items="${reviews}">
      <table class="table table-striped">
        <tr>
          <td width="25%">Reviewer</td>
          <td><c:out value="${review._uId}"/></td>
        </tr>
        <tr>
          <td>Comments</td>
          <td><c:out value="${review.comments}"/></td>
        </tr>
        <tr>
          <td>Decision</td>
          <td><c:out value="${review.decision}"/></td>
        </tr>
      </table>
    </c:forEach>
    <div class="row">
      <a href="<c:url value="/jobs/${application._jobId}/applications" />">
        <button class="btn btn-default">Back to list</button>
      </a>
    </div>
  </div>
  <%@ include file="footer.html" %>
</body>
</html>