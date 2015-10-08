<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
    
    <hgroup class="mb20">
      <h1>Update Job</h1>
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
          <input type="text" class="form-control" name="positionType" value="${job.positionType}" required>
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-4">
          <label for="location">Location</label>
          <input type="text" class="form-control" name="location" value="${job.location}" required>
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-4">
          <label for="salary">Salary</label>
          <div class="input-group">
            <div class="input-group-addon">$</div>
            <input type="text" class="form-control" name="salary"
            value="${job.salary}"
            required pattern="\d+"
            title="Salary must be whole number">
          </div>
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-4">
          <label for="closingDate">Closing Date</label>
          <input type="date" class="form-control" name="closingDate" value="${job.closingDate}" required 
          <c:if test="${job.closingDate lt today}">readonly</c:if> />
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-8">
          <label for="description">Description</label>
          <textarea class="form-control" rows="10" name="description" maxLength="1000" required>${job.description}</textarea>
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