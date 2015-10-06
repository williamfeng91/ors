<%@ include file="header.jsp" %>
<body class="login">
  <%@ include file="headerBar.jsp" %>
  <div class="container">
  
    <hgroup class="mb20">
      <center><h1>ORS</h1></center>
      <c:choose>
        <c:when test="${not empty errorMsg}" >
          <h2 class="lead error-msg">${errorMsg}</h2>
        </c:when>
        <c:when test="${not empty successMsg}" >
          <h2 class="lead success-msg" style="color:#fff !important;">${successMsg}</h2>
        </c:when>
      </c:choose>
    </hgroup>
    
    <div class="card card-container">
      <img id="profile-img" class="profile-img-card" src="//ssl.gstatic.com/accounts/ui/avatar_2x.png" />
      <p id="profile-name" class="profile-name-card"></p>
      <form class="form-signin" action="<c:url value="/doLogin" />" method="post">
        <input type="text" id="username" name="username" class="form-control" placeholder="Username" required autofocus>
        <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
        <button class="btn-lg btn-block btn-signin" type="submit">Sign in</button>
      </form>
    </div>
  </div>
  <%@ include file="footer.html" %>
</body>
</html>