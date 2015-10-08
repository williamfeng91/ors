<nav class="navbar navbar-inverse navbar-fixed-top">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="<c:url value="/" />">ORS</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <form class="navbar-form navbar-left" action="<c:url value="/advSearchJob" />" method="post">
        <div class="form-group">
          <input type="text" class="form-control" placeholder="position" name="positionType">
        </div>
        <button type="submit" class="btn btn-default">Search</button>
      </form>
      <div class="navbar-form navbar-left">
        <a href="<c:url value="/advSearchJob" />"><button type="button" class="btn btn-danger">Advanced Search</button></a>
      </div>
      <ul class="nav navbar-nav navbar-right">
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Browse<span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="<c:url value="/jobs" />">Job Postings</a></li>
            <c:if test="${not empty user && user.role eq 'manager'}">
              <li><a href="<c:url value="/applications" />">Applications</a></li>
            </c:if>
          </ul>
        </li>
      <c:choose>
        <c:when test="${not empty user}">
            <c:if test="${user.role eq 'manager'}">
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Create<span class="caret"></span></a>
                <ul class="dropdown-menu" role="menu">
                  <li><a href="<c:url value="/jobs/new" />">Job Posting</a></li>
                </ul>
              </li>
            </c:if>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">${user.firstName}<span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
                <li><a href="<c:url value="/logout" />">Log out</a></li>
              </ul>
            </li>
        </c:when>
        <c:otherwise>
          <li><a href="<c:url value="/login" />">Login</a></li>
        </c:otherwise>
      </c:choose>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>