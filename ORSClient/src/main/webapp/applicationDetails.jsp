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
          <c:if test="${empty user}">
            <h2 class="lead error-msg"><p>Please bookmark this page or note down the following link:<br/>
              <a>http://localhost:8080/ORSClient/applications/${application._appId}</a></p>
              <p>You can check the latest status of your application here.</p>
            </h2>
          </c:if>
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
        <td>Name</td>
        <td><c:out value="${application.name}"/></td>
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
    <c:if test="${empty user}">
      <div class="row">
        <c:if test="${application.status eq 'CREATED'}">
          <p>You may update or withdraw your application before the recruitment team starts processing.</p>
          <a href="<c:url value="/applications/${application._appId}/edit" />"><button class="btn btn-default">Edit</button></a>
          <a href="<c:url value="/applications/${application._appId}/delete" />"><button class="btn btn-default">Withdraw</button></a>
        </c:if>
        <c:if test="${application.status eq 'IN_REVIEW'}">
          <p>You application is under review.</p>
        </c:if>
        <c:if test="${application.status eq 'REVIEWED'}">
          <p>You application has been reviewed. Please wait for further notices.</p>
        </c:if>
        <c:if test="${application.status eq 'SHORTLISTED'}">
          <p>Congratulations! You have received an invitation for interview. Please choose to accept or reject.</p>
          <a href="<c:url value="/applications/${application._appId}/acceptInvitation" />"><button class="btn btn-default">Accept</button></a>
          <a href="<c:url value="/applications/${application._appId}/rejectInvitation" />"><button class="btn btn-default">Reject</button></a>
        </c:if>
        <c:if test="${application.status eq 'ACCEPTED_INVITATION'}">
          <p>You have accept the invitation. The recruitment team will contact you soon.</p>
        </c:if>
        <c:if test="${application.status eq 'FINALISED'}">
          <p>Your application has been finalised.</p>
          <a href="<c:url value="/applications/${application._appId}/delete" />"><button class="btn btn-default">Archive</button></a>
        </c:if>
      </div>
    </c:if>
  </div>
  <%@ include file="footer.html" %>
</body>
</html>