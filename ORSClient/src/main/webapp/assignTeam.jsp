<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
    
    <hgroup class="mb20">
      <h1>Assign Hiring Team</h1>
      <c:choose>
        <c:when test="${not empty errorMsg}" >
          <h2 class="lead error-msg">${errorMsg}</h2>
        </c:when>
        <c:when test="${not empty successMsg}" >
          <h2 class="lead success-msg">${successMsg}</h2>
        </c:when>
        <c:otherwise>
          <h2 class="lead">Please fill in the following details</h2>
        </c:otherwise>
      </c:choose>
    </hgroup>

    <form action="<c:url value="/jobs/${job._jobId}/update" />" method="post">
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-8">
          <label for="positionType">Position Type</label>
          <input type="text" class="form-control" name="positionType" value="${job.positionType}" readonly>
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-4">
          <label for="assignedTeam">Assigned Team</label>
          <select name="assignedTeam">
            <c:choose>
              <c:when test="${empty job.assignedTeam}">
                <option value="" selected="selected"></option>
              </c:when>
              <c:otherwise>
                <option value=""></option>
              </c:otherwise>
            </c:choose>
            <c:forEach var="team" items="${teams}">
              <c:choose>
                <c:when test="${team eq job.assignedTeam}">
                  <option value="${team}" selected="selected">${team}</option>
                </c:when>
                <c:otherwise>
                  <option value="${team}">${team}</option>
                </c:otherwise>
              </c:choose>
            </c:forEach>
          </select>
        </div>
      </div>
      <div class="row">
        <button type="submit" class="btn btn-default">Submit</button>
        <a href="<c:url value="/jobs/${job._jobId}" />"><button type="button" class="btn btn-default">Cancel</button></a>
      </div>
    </form>
  </div>
<%@ include file="footer.html" %>
</body>
</html>