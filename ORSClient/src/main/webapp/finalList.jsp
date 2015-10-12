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
          <h2 class="lead">Please select the candidate</h2>
        </c:otherwise>
      </c:choose>
    </hgroup>

    <form action="<c:url value="/jobs/${job._jobId}/finalList" />" method="post">
      <table class="table table-striped">
        <tr>
          <td width="50%">Candidate</td>
          <td>Final Decision</td>
        </tr>
        <c:forEach var="application" items="${applications}">
          <tr>
            <td><a href="<c:url value="/applications/${application._appId}" />"><c:out value="${application.fullName}"/></a></td>
            <td>
              <input type="radio" name="selectedCandidate" value="${application._appId}" />
            </td>
          </tr>
        </c:forEach>
      </table>
      <div class="row">
        <button type="submit" class="btn btn-default">Submit</button>
        <a href="<c:url value="/jobs" />"><button type="button" class="btn btn-default">Cancel</button></a>
      </div>
    </form>
  </div>
<%@ include file="footer.html" %>
</body>
</html>