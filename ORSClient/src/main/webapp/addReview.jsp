<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
    
    <hgroup class="mb20">
      <center><h1>Post New Review</h1></center>
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

    <form action="<c:url value="/applications/${application._appId}/reviews" />" method="post">
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-8">
          <label for="comments">Comments</label>
          <textarea class="form-control" rows="10" name="comments" maxLength="1000" required>${review.comments}</textarea>
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-12 col-sm-12 col-md-4">
          <label for="decision">Decision</label>
          <select name="decision">
            <c:forEach var="decision" items="${decisions}">
              <c:choose>
                <c:when test="${decision eq review.decision}">
                  <option value="${decision}" selected="selected">${decision}</option>
                </c:when>
                <c:otherwise>
                  <option value="${decision}">${decision}</option>
                </c:otherwise>
              </c:choose>
            </c:forEach>
          </select>
        </div>
      </div>
      <div class="row">
        <button type="submit" class="btn btn-default">Submit</button>
        <a href="<c:url value="/jobs/${application._jobId}/applications" />"><button type="button" class="btn btn-default">Cancel</button></a>
      </div>
    </form>
  </div>
<%@ include file="footer.html" %>
</body>
</html>