<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
    
    <hgroup class="mb20">
      <h1>Shortlist for ${job.positionType}</h1>
      <c:choose>
        <c:when test="${not empty errorMsg}" >
          <h2 class="lead error-msg">${errorMsg}</h2>
        </c:when>
        <c:when test="${not empty successMsg}" >
          <h2 class="lead success-msg">${successMsg}</h2>
        </c:when>
        <c:otherwise>
          <h2 class="lead">Please select applicants for your final shortlist</h2>
        </c:otherwise>
      </c:choose>
    </hgroup>

    <form action="<c:url value="/jobs/${job._jobId}/shortlist" />" method="post">
      <table class="table table-striped">
        <tr>
          <td width="50%">Applicant</td>
          <td>Shortlist or not</td>
        </tr>
        <c:forEach var="application" items="${applications}">
          <tr>
            <td><a href="<c:url value="/applications/${application._appId}/reviews" />"><c:out value="${application.fullName}"/></a></td>
            <td>
              <input type="checkbox" name="${application._appId}" <c:if test="${application.isShortlistedByAllReviewers()}">checked="checked"</c:if> />
            </td>
          </tr>
        </c:forEach>
      </table>
      <c:if test="${job.status eq 'IN_REVIEW'}">
        <div class="row">
          <button type="submit" class="btn btn-default">Send Invitations</button>
          <a href="<c:url value="/jobs" />"><button type="button" class="btn btn-default">Cancel</button></a>
        </div>
      </c:if>
    </form>
  </div>
<%@ include file="footer.html" %>
</body>
</html>