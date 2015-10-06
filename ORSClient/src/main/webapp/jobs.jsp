<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
  
    <hgroup class="mb20">
      <h1>Job Postings</h1>
      <c:choose>
        <c:when test="${not empty errorMsg}">
          <h2 class="lead error-msg">${errorMsg}</h2>
        </c:when>
        <c:otherwise>
          <h2 class="lead"><strong class="text-danger">${fn:length(jobs)}</strong> jobs were found</h2>
        </c:otherwise>
      </c:choose>
    </hgroup>

    <c:forEach var="job" items="${jobs}">
      <section class="col-xs-12 col-sm-6 col-md-12">
        <div class="search-result row">
              <div class="col-xs-12 col-sm-12 col-md-3">
                <a href="<c:url value="/jobs/${job._jobId}" />" class="thumbnail"><img src="<c:url value="/resources/images/no-img.jpg" />" alt="${job.location}-${job.positionType}" /></a>
              </div>
              <div class="col-xs-12 col-sm-12 col-md-2">
                <ul class="meta-search">
                  <li><a href="<c:url value="/jobs/${job._jobId}" />"><span>View job details</span></a></li>
                  <c:if test="${not empty user}">
                    <li><a href="<c:url value="/jobs/${job._jobId}/applications" />"><span>View applications</span></a></li>
                  </c:if>
                  <c:if test="${not empty user && user.role == 'manager'}">
                    <li><a href="<c:url value="/jobs/${job._jobId}/edit" />"><span>Edit</span></a></li>
                    <li><a href="<c:url value="/jobs/${job._jobId}/delete" />"><span>Delete</span></a></li>
                  </c:if>
                </ul>
              </div>
              <div class="col-xs-12 col-sm-12 col-md-7 excerpet">
                <h3><a href="<c:url value="/jobs/${job._jobId}" />" title="">${job.positionType}</a></h3>
                <p>Location: ${job.location}</p>
                <p>Salary: <fmt:formatNumber value="${job.salary}" type="currency"/></p>
                <p>Closing date: ${job.closingDate}</p>
                <c:if test="${not empty user}">
                  <p>Status: ${job.status}</p>
                  <p>Assigned Team: ${job.assignedTeam}</p>
                </c:if>
              </div>
              <span class="clearfix borda"></span>
            </div>
      </section>
    </c:forEach>
  </div>
  <%@ include file="footer.html" %>
</body>
</html>