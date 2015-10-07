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
                <a href="<c:url value="/jobs/${job._jobId}" />" class="thumbnail">
                  <img src="<c:url value="/resources/images/no-img.jpg" />" alt="${job.location}-${job.positionType}" />
                </a>
              </div>
              <div class="col-xs-12 col-sm-12 col-md-2 left-col">
                <ul class="meta-search">
                  <li><a href="<c:url value="/jobs/${job._jobId}" />"><span>View job details</span></a></li>
                  <c:if test="${not empty user}">
                    <li>
                      <a href="<c:url value="/jobs/${job._jobId}/applications" />">
                        <span>View applications</span>
                      </a>
                    </li>
                  </c:if>
                  <c:if test="${not empty user && user.role eq 'manager' && job.status eq 'CREATED'}">
                    <li><a href="<c:url value="/jobs/${job._jobId}/edit" />"><span>Edit</span></a></li>
                  </c:if>
                  <c:if test="${not empty user && user.role eq 'manager' && (job.status eq 'CREATED' || job.status eq 'FINALISED')}">
                    <li><a href="<c:url value="/jobs/${job._jobId}/delete" />"><span>Delete</span></a></li>
                  </c:if>
                </ul>
              </div>
              <div class="col-xs-12 col-sm-12 col-md-5 left-col">
                <h3><a href="<c:url value="/jobs/${job._jobId}" />" title="">${job.positionType}</a></h3>
                <p>Location: ${job.location}</p>
                <p>Salary: <fmt:formatNumber value="${job.salary}" type="currency"/></p>
                <p>Closing date: ${job.closingDate}</p>
                <c:if test="${not empty user}">
                  <p>Status: ${job.status}</p>
                  <c:if test="${job.status ne 'CREATED'}">
                    <p>Assigned Team: ${job.assignedTeam}</p>
                  </c:if>
                </c:if>
              </div>
              <div class="col-xs-12 col-sm-12 col-md-2 right-most">
                <c:if test="${not empty user && user.role eq 'manager'}">
                  <c:choose>
                    <c:when test="${job.status eq 'CREATED'}">
                      <c:choose>
                        <c:when test="${job.closingDate ge today}">
                          <p><button type="button" class="btn btn-default disabled">Application Open</button></p>
                        </c:when>
                        <c:otherwise>
		                  <p><a href="<c:url value="/jobs/${job._jobId}/startAutoChecks" />">
		                    <button type="button" class="btn btn-default">Start Auto-check</button>
		                  </a></p>
		                  <p><a href="<c:url value="/jobs/${job._jobId}/edit" />">
		                    <button type="button" class="btn btn-default">Assign Hiring Team</button>
		                  </a></p>
		                </c:otherwise>
		              </c:choose>
                    </c:when>
                    <c:when test="${job.status eq 'IN_REVIEW'}">
                      <p><a href="<c:url value="/jobs/${job._jobId}/applications" />">
                        <button type="button" class="btn btn-default">View Review Status</button>
                      </a></p>
                    </c:when>
                    <c:when test="${job.status eq 'PROCESSED'}">
                    </c:when>
                    <c:when test="${job.status eq 'SENT_INVITATIONS'}">
                    </c:when>
                    <c:when test="${job.status eq 'FINALISED'}">
                    </c:when>
                    <c:when test="${job.status eq 'ARCHIVED'}">
                    </c:when>
                  </c:choose>
                </c:if>
                <c:if test="${not empty user && user.role eq 'reviewer'}">
                  <p><a href="<c:url value="/jobs/${job._jobId}/applications" />">
                    <button type="button" class="btn btn-default">View Applications</button>
                  </a></p>
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