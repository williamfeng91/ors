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
                <a href="<c:url value="applications/${application._appId}" />" class="thumbnail"><img src="<c:url value="/resources/images/no-img.jpg" />" alt="${application._appId}" /></a>
              </div>
              <div class="col-xs-12 col-sm-12 col-md-2">
                <ul class="meta-search">
                  <li><a href="<c:url value="applications/${application._appId}" />"><span>View application details</span></a></li>
                  <c:if test="${not empty job}">
                    <li><a href="<c:url value="jobs/${job._jobId}" />"><span>View job posting</span></a></li>
                  </c:if>
                </ul>
              </div>
              <div class="col-xs-12 col-sm-12 col-md-7 excerpet">
                <h3><a href="<c:url value="applications/${application._appId}" />" title="">${application._appId}</a></h3>
                <c:if test="${not empty job}">
                  <p>Job: ${job.positionType}</p>
                </c:if>
                <p>Personal Details: ${application.personalDetails}"</p>
                <p>Status: ${application.status}</p>
              </div>
              <span class="clearfix borda"></span>
            </div>
      </section>
    </c:forEach>
  </div>
  <%@ include file="footer.html" %>
</body>
</html>