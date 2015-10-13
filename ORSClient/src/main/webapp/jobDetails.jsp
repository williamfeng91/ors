<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
    
    <hgroup class="mb20">
      <h1>Job Details</h1>
      <c:choose>
        <c:when test="${not empty errorMsg}" >
          <h2 class="lead error-msg">${errorMsg}</h2>
        </c:when>
        <c:when test="${not empty successMsg}" >
          <h2 class="lead success-msg">${successMsg}</h2>
        </c:when>
      </c:choose>
    </hgroup>
    
    <table class="table table-striped">
      <tr>
        <td width="25%">Position Type</td>
        <td><c:out value="${job.positionType}"/></td>
      </tr>
      <tr>
        <td>Location</td>
        <td><c:out value="${job.location}"/></td>
      </tr>
      <tr>
        <td>Salary</td>
        <td><fmt:formatNumber value="${job.salary}" type="currency"/></td>
      </tr>
      <tr>
        <td>Closing Date</td>
        <td><c:out value="${job.closingDate}"/></td>
      </tr>
      <tr>
        <td>Description</td>
        <td><c:out value="${job.description}"/></td>
      </tr>
      <c:if test="${not empty user}">
        <tr>
          <td>Status</td>
          <td><c:out value="${job.status}"/></td>
        </tr>
        <tr>
          <td>Assigned Team</td>
          <td><c:out value="${job.assignedTeam}"/></td>
        </tr>
      </c:if>
    </table>
    <div class="row">
      <a href="<c:url value="/jobs" />">
        <button class="btn btn-default">Back to list</button>
      </a>
      <c:choose>
        <c:when test="${not empty user && user.role eq 'manager'}">
          <c:if test="${job.status eq 'CREATED' && !job.hasReceivedApplication()}">
            <a href="<c:url value="/jobs/${job._jobId}/edit" />"><button class="btn btn-default">Edit</button></a>
          </c:if>
          <c:if test="${job.status eq 'FINALISED'}">
            <a href="<c:url value="/jobs/${job._jobId}/delete" />"><button class="btn btn-default">Delete</button></a>
          </c:if>
        </c:when>
        <c:otherwise>
          <c:if test="${job.closingDate ge today}">
            <a href="<c:url value="/jobs/${job._jobId}/apply" />"><button class="btn btn-default">Apply</button></a>
          </c:if>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
  <%@ include file="footer.html" %>
</body>
</html>