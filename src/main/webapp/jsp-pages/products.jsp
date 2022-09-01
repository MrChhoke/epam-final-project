<%@ page import="com.epam.cash.register.entity.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="com.epam.cash.register.service.ProductService" %>
<%@ page import="com.epam.cash.register.service.ProductServiceImpl" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post" action="/products">
    <label>Ukrainian Title</label>
    <input name="title_ukr"/>
    <label>English Title</label>
    <input name="title_eng"/>
    <label>Price</label>
    <input name="price"/>
    <label>Quantity</label>
    <input name="quantity"/>
    <label>Code</label>
    <input name="code"/>
    <button>Send Info</button>
</form>

<%!
    private final ProductService productService = new ProductServiceImpl();
%>

<% List<Product> products = productService.findAll();
    if (products.size() == 0) {
        out.println("The product list is empty");
    } else {%>
<table>
    <tr>
        <th>ID</th>
        <th>Ukrainian Title</th>
        <th>English Title</th>
        <th>Price</th>
        <th>Quantity</th>
        <th>Code</th>
    </tr>
    <%
        }
        for (Product product : products) {
    %>
    <tr>
        <td><%=product.getId()%></td>
        <td><%=product.getTitle_ukr()%></td>
        <td><%=product.getTitle_eng()%></td>
        <td><%=product.getPrice()%></td>
        <td><%=product.getQuantity()%></td>
        <td><%=product.getCode()%></td>
    </tr>
    <% } %>
    <%if(products.size() != 0){
        out.println("</table>");
    }%>
</body>
</html>
