<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
  
    <hgroup class="mb20">
      <h1>Applications<c:if test="${not empty job}"> for <a href="<c:url value="/jobs/${job._jobId}" />">${job.positionType}</a></c:if></h1>
      <c:choose>
        <c:when test="${not empty errorMsg}">
          <h2 class="lead error-msg">${errorMsg}</h2>
        </c:when>
        <c:otherwise>
          <h2 class="lead"><strong class="text-danger">${fn:length(reviewedApplications)+fn:length(applications)}</strong> applications were found</h2>
        </c:otherwise>
      </c:choose>
    </hgroup>
    
    <c:forEach var="application" items="${applications}">
      <section class="col-xs-12 col-sm-6 col-md-12">
        <div class="search-result row">
              <div class="col-xs-12 col-sm-12 col-md-3">
                <a href="<c:url value="/applications/${application._appId}" />" class="thumbnail"><img src="<c:url value="/resources/images/no-img.jpg" />" alt="${application._appId}" /></a>
              </div>
              <div class="col-xs-12 col-sm-12 col-md-2 left-col">
                <ul class="meta-search">
                  <c:if test="${not empty user && application.status ne 'CREATED'}">
                    <li><a href="<c:url value="/applications/${application._appId}/autoCheckResults" />"><span>View auto-check results</span></a></li>
                  </c:if>
                  <c:if test="${not empty user && user.role eq 'manager' && application.status ne 'CREATED'}">
                    <li><a href="<c:url value="/applications/${application._appId}/reviews" />"><span>View reviews</span></a></li>
                  </c:if>
                </ul>
              </div>
              <div class="col-xs-12 col-sm-12 col-md-5 left-col">
                <h3><a href="<c:url value="/applications/${application._appId}" />" title="">${application.name}</a></h3>
                <p>Status: ${application.status}</p>
              </div>
              <div class="col-xs-12 col-sm-12 col-md-2 right-most">
                <c:if test="${not empty user && user.role eq 'manager'}">
                  <c:choose>
                    <c:when test="${application.status eq 'CREATED'}">
                      <p><button type="button" class="btn btn-default disabled">Review Not Started</button></p>
                    </c:when>
                    <c:when test="${application.status eq 'IN_REVIEW'}">
                      <p><button type="button" class="btn btn-default disabled">In Review</button></p>
                    </c:when>
                    <c:when test="${application.status eq 'REVIEWED'}">
                      <p><a href="<c:url value="/applications/${application._appId}/reviews" />">
                        <button type="button" class="btn btn-default">Review Completed</button>
                      </a></p>
                    </c:when>
                    <c:when test="${application.status eq 'SHORTLISTED'}">
                      <p><button type="button" class="btn btn-default disabled">Shortlisted</button></p>
                    </c:when>
                    <c:when test="${application.status eq 'ACCEPTED_INVITATION'}">
                      <p><button type="button" class="btn btn-default disabled">Invitation Accepted</button></p>
                    </c:when>
                    <c:when test="${application.status eq 'FINALISED'}">
                      <p><a href="<c:url value="/applications/${application._appId}/delete" />">
                        <button type="button" class="btn btn-default">Archive</button>
                      </a></p>
                    </c:when>
                    <c:when test="${application.status eq 'ARCHIVED'}">
                      <p><button type="button" class="btn btn-default disabled">Archived</button></p>
                    </c:when>
                  </c:choose>
                </c:if>
                <c:if test="${not empty user && user.role eq 'reviewer'}">
                  <c:choose>
                    <c:when test="${application.status eq 'CREATED'}">
                      <p><button type="button" class="btn btn-default disabled">Waiting for Auto-check</button></p>
                    </c:when>
                    <c:when test="${application.status eq 'IN_REVIEW'}">
                      <p><a href="<c:url value="/applications/${application._appId}/review" />">
                        <button type="button" class="btn btn-default">Review</button>
                      </a></p>
                    </c:when>
                    <c:otherwise>
                      <p><button type="button" class="btn btn-default disabled">Reviewed</button></a></p>
                    </c:otherwise>
                  </c:choose>
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