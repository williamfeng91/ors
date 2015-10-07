<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
  
    <hgroup class="mb20">
      <h1>Applications</h1>
      <c:choose>
        <c:when test="${not empty errorMsg}">
          <h2 class="lead error-msg">${errorMsg}</h2>
        </c:when>
        <c:otherwise>
          <h2 class="lead"><strong class="text-danger">${fn:length(applications)}</strong> applications were found</h2>
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
                  <li><a href="<c:url value="/applications/${application._appId}" />"><span>View application details</span></a></li>
                  <c:if test="${not empty job}">
                    <li><a href="<c:url value="/jobs/${job._jobId}" />"><span>View job posting</span></a></li>
                  </c:if>
                  <c:if test="${not empty user && user.role == 'reviewer'}">
                    <li><a href="<c:url value="/applications/${application._appId}/autoCheckResults" />"><span>View auto-check results</span></a></li>
                  </c:if>
                </ul>
              </div>
              <div class="col-xs-12 col-sm-12 col-md-5 left-col">
                <h3><a href="<c:url value="/applications/${application._appId}" />" title="">${application._appId}</a></h3>
                <c:if test="${not empty job}">
                  <p>Job: ${job.positionType}</p>
                </c:if>
                <p>Personal Details: ${application.personalDetails}"</p>
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
                      <p><button type="button" class="btn btn-default disabled">Review Completed</button></p>
                    </c:when>
                    <c:when test="${application.status eq 'RECEIVED_INVITATION'}">
                    </c:when>
                    <c:when test="${application.status eq 'FINALISED'}">
                    </c:when>
                    <c:when test="${application.status eq 'ARCHIVED'}">
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
                    <c:when test="${application.status eq 'REVIEWED'}">
                      <p><button type="button" class="btn btn-default disabled">Review Completed</button></p>
                    </c:when>
                    <c:when test="${application.status eq 'RECEIVED_INVITATION'}">
                    </c:when>
                    <c:when test="${application.status eq 'FINALISED'}">
                    </c:when>
                    <c:when test="${application.status eq 'ARCHIVED'}">
                    </c:when>
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