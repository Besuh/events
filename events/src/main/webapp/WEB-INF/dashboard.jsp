<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dashboard</title>
</head>
<body>
    <h1>Welcome <c:out value="${currentUser.firstName}"></c:out>!</h1>
 
    <h3>First Name: <span>${currentUser.firstName }</span></h3>
    <h3>Last Name: <span>${currentUser.lastName }</span></h3>
    <h3>Email: <span>${currentUser.username }</span></h3>
    <h3>Sign Up Date: <span><fmt:formatDate pattern = "EEEEE, MMM d, yyyy" value = "${currentUser.createdAt }" /> at <fmt:formatDate type = "time" timeStyle = "short" value = "${currentUser.createdAt}" /></span></h3>
    <h3>Last Sign In: <span><fmt:formatDate pattern = "EEEEE, MMM d, yyyy" value = "${currentUser.updatedAt }" /> at <fmt:formatDate type = "time" timeStyle = "short" value = "${currentUser.updatedAt}" /></span></h3>
    
    <form id="logoutForm" method="POST" action="/logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="submit" value="Logout" />
    </form>
    
</body>
</html>