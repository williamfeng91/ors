<%@ include file="header.jsp" %>
<body>
  <%@ include file="headerBar.jsp" %>
  <div class="container">
    
    <hgroup class="mb20">
      <center><h1>An error has occurred!</h1></center>
      <h2 class="lead error-msg">${errorMsg}</h2>
    </hgroup>
    
  </div>
  <%@ include file="footer.html" %>
</body>
</html>