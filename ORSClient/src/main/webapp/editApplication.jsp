<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
    
    <hgroup class="mb20">
      <h1>Update Application</h1>
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

    <form action="<c:url value="/applications/${application._appId}/update" />" method="post">
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-8">
          <label for="job">Job</label>
          <input type="text" class="form-control" name="job" value="${job.positionType}" readonly>
          <input type="hidden" class="form-control" name="_jobId" value="${job._jobId}">
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-8">
          <label for="licenseNo">License Number</label>
          <input type="text" class="form-control" name="licenseNo" value="${application.licenseNo}" />
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-8">
          <label for="fullName">Full Name</label>
          <input type="text" class="form-control" name="fullName" value="${application.fullName}" />
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-8">
          <label for="postcode">Postcode</label>
          <input type="text" class="form-control" name="postcode" value="${application.postcode}" />
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-8">
          <label for="cv">CV</label>
          <textarea class="form-control" rows="10" name="cv" maxLength="1000" required>${application.cv}</textarea>
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-8">
          <label for="resume">Resume</label>
          <textarea class="form-control" rows="10" name="resume" maxLength="1000" required>${application.resume}</textarea>
        </div>
      </div>
      <div class="row">
        <button type="submit" class="btn btn-default">Submit</button>
        <a href="<c:url value="/applications/${application._appId}" />"><button type="button" class="btn btn-default">Cancel</button></a>
      </div>
    </form>
  </div>
<%@ include file="footer.html" %>
</body>
</html>