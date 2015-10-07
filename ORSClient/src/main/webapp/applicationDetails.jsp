<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
    
    <hgroup class="mb20">
      <center><h1>Application Details</h1></center>
      <c:choose>
        <c:when test="${not empty errorMsg}" >
          <h2 class="lead error-msg">${errorMsg}</h2>
        </c:when>
        <c:when test="${not empty successMsg}" >
          <h2 class="lead success-msg">${successMsg}</h2>
        </c:when>
        <c:otherwise>
          <h2 class="lead error-msg"><p>Please bookmark this page or note down the following link:<br/>
            <a>http://localhost:8080/ORSClient/applications/${application._appId}</a></p>
            <p>You can check the latest status of your application here.</p>
          </h2>
        </c:otherwise>
      </c:choose>
    </hgroup>
    
    <table class="table table-striped">
      <tr>
        <td width="25%">Job</td>
        <td><a href="<c:url value="/jobs/${job._jobId}" />"><c:out value="${job.positionType}"/></a></td>
      </tr>
      <tr>
        <td>Status</td>
        <td><c:out value="${application.status}"/></td>
      </tr>
      <tr>
        <td>Personal Details</td>
        <td><c:out value="${application.personalDetails}"/></td>
      </tr>
      <tr>
        <td>CV</td>
        <td><c:out value="${application.cv}"/></td>
      </tr>
      <tr>
        <td>Resume</td>
        <td><c:out value="${application.resume}"/></td>
      </tr>
    </table>
    <div class="row">
      <c:if test="${application.status eq 'CREATED'}">
        <a href="<c:url value="/applications/${application._appId}/edit" />"><button class="btn btn-default">Edit</button></a>
      </c:if>
      <c:if test="${application.status eq 'FINALISED'}">
        <a href="<c:url value="/applications/${application._appId}/delete" />"><button class="btn btn-default">Archive</button></a>
      </c:if>
    </div>
  </div>
  <%@ include file="footer.html" %>
</body>
</html>