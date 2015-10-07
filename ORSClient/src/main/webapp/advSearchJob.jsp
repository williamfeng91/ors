<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
    
    <hgroup class="mb20">
      <center><h1>Advanced Search</h1></center>
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

    <form action="<c:url value="/advSearchJob" />" method="post">
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-8">
          <label for="positionType">Position Type</label>
          <input type="text" class="form-control" id="positionType" name="positionType" />
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-8">
          <label for="location">Location</label>
          <input type="text" class="form-control" id="location" name="location" />
        </div>
      </div>
      <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-3">
          <label>Salary Range</label>
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-3">
          <div class="input-group">
            <div class="input-group-addon">$</div>
            <input type="text" class="form-control" id="salaryFrom" name="salaryFrom"
            pattern="\d+"
            title="Salary must be whole numbers" />
          </div>
        </div>
        <div class="form-group col-xs-12 col-sm-12 col-md-1">-</div>
        <div class="form-group col-xs-12 col-sm-12 col-md-3">
          <div class="input-group">
            <div class="input-group-addon">$</div>
            <input type="text" class="form-control" id="salaryTo" name="salaryTo"
            pattern="\d+"
            title="Salary must be whole numbers" />
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-3">
          <label>Closing Date Range</label>
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-3">
          <input type="date" class="form-control" id="closingDateFrom" name="closingDateFrom" />
        </div>
        <div class="form-group col-xs-12 col-sm-12 col-md-1">-</div>
        <div class="form-group col-xs-12 col-sm-12 col-md-3">
          <input type="date" class="form-control" id="closingDateTo" name="closingDateTo" />
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-8">
          <label for="description">Description</label>
          <input type="text" class="form-control" id="description" name="description" />
        </div>
      </div>
      <c:if test="${not empty user}">
        <div class="row">
          <div class="form-group col-xs-12 col-sm-12 col-md-3">
            <label for="status">Status</label>
            <select class="form-control" id="status" name="status">
              <option value="" selected>ALL</option>
              <c:forEach var="status" items="${statuses}">
                <option value="${status}">${status}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <div class="row">
          <div class="form-group col-xs-12 col-sm-12 col-md-3">
            <label for="team">Assigned Team</label>
            <select class="form-control" id="team" name="team">
              <option value="" selected>ALL</option>
              <c:forEach var="team" items="${teams}">
                <option value="${team}">${team}</option>
              </c:forEach>
            </select>
          </div>
        </div>
      </c:if>
      <button type="submit" class="btn btn-default">Submit</button>
    </form>
  </div>
  <%@ include file="footer.html" %>
</body>
</html>