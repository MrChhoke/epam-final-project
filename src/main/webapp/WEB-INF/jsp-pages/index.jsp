<%@ page import="com.epam.cash.register.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<h1>Welcome page</h1>
<% User user = (User) session.getAttribute("user");
    if (user == null) {%>
<h2><a href="/login">Login</a></h2>
<h2><a href="/reg">Registration</a></h2>
<%} else {%>
<h3><a href="/products">Products</a></h3>
<%if(user.getRole() == User.Role.CASHIER
        || user.getRole() == User.Role.SENIOR_CASHIER
        || user.getRole() == User.Role.ADMIN){%>
    <h3><a href="/receipt">Receipts</a></h3>
<%}%>
<h2><a href="/exit">Exit</a></h2>
<%}%>
</body>
</html>
