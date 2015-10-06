<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Loan Approval Client Application</title>
</head>
<body>
	<a href="index.jsp">Home</a>
	<h1>Loan Request Test</h1>
	<form id="TestForm" action="submitRequest">
		<input type="text" name="firstName"/>
        <input type="text" name="lastName"/>
        <input type="text" name="amount"/>
        <input type="submit" />
    </form>
</body>
</html>
