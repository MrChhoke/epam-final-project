<%@ page import="com.epam.cash.register.entity.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="com.epam.cash.register.entity.User" %>
<%@ page import="com.epam.cash.register.service.ProductService" %>
<%@ page import="com.epam.cash.register.service.ProductServiceImpl" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Products</title>
    <link rel="stylesheet" href="/node_modules/bootstrap/dist/css/bootstrap.min.css">
    <script src="/node_modules/jquery/dist/jquery.min.js"></script>
    <script src="/node_modules/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="container">
<%  User user = (User) session.getAttribute("user");
    if(user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.CASHIER){%>
<form method="post" action="/products">
    <div class="form-group">
        <label class="">Ukrainian Title</label>
        <input class="form-control" name="title_ukr" type="text"/>
    </div>
    <div class="form-group">
        <label>English Title</label>
        <input class="form-control" name="title_eng"/>
    </div>
    <div class="form-group">
        <label>Price</label>
        <input class="form-control" name="price"/>
    </div>
    <div class="form-group">
        <label>Quantity</label>
        <input class="form-control" name="quantity"/>
    </div>
    <div class="form-group">
        <label>Code</label>
        <input class="form-control" name="code"/>
    </div>
    <button class="btn btn-success">Send Info</button>
</form>
<%}%>

<%  List<Product> products = (List<Product>) request.getAttribute("all_products");
    if (products.size() == 0) {
        out.println("The product list is empty");
    } else {%>
<table id="table" class="table">
    <tr>
        <th scope="col">ID</th>
        <th scope="col">Ukrainian Title</th>
        <th scope="col">English Title</th>
        <th scope="col">Price</th>
        <th scope="col">Quantity</th>
        <th scope="col">Code</th>
        <th scope="col"></th>
        <th scope="col"></th>
    </tr>
    <%
        }
        for (Product product : products) {
    %>
    <tr id="<%=product.getId()%>">
        <td scope="row"><%=product.getId()%></td>
        <td id="element_ukrTitle_<%=product.getId()%>"><%=product.getTitle_ukr()%></td>
        <td id="element_engTitle_<%=product.getId()%>"><%=product.getTitle_eng()%></td>
        <td id="element_price_<%=product.getId()%>"><%=product.getPrice()%></td>
        <td id="element_quantity_<%=product.getId()%>"><%=product.getQuantity()%></td>
        <td id="element_code_<%=product.getId()%>"><%=product.getCode()%></td>
        <td><button type="button" class="btn btn-danger">Delete</button></td>
        <td><button type="button" class="btn btn-info" data-toggle="modal" data-target="#exampleModal">Edit</button></td>
    </tr>
    <% } %>
    <%if(products.size() != 0){
        out.println("</table>");
    }%>
</div>
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="ProductTitle">Modal title</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label for="changed_element_ukrTitle">Ukrainian Title</label>
                        <input id="changed_element_ukrTitle" class="form-control" name="title_ukr" type="text"/>
                    </div>
                    <div class="form-group">
                        <label for="changed_element_engTitle">English Title</label>
                        <input id="changed_element_engTitle" class="form-control" name="title_eng"/>
                    </div>
                    <div class="form-group">
                        <label for="changed_element_price">Price</label>
                        <input id="changed_element_price" class="form-control" name="price"/>
                    </div>
                    <div class="form-group">
                        <label for="changed_element_quantity">Quantity</label>
                        <input id="changed_element_quantity" class="form-control" name="quantity"/>
                    </div>
                    <div class="form-group">
                        <label for="changed_element_code">Code</label>
                        <input id="changed_element_code" class="form-control" name="code"/>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                <button type="button" id="changedInfoButton" class="btn btn-warning">Save changes</button>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    const table = document.getElementById('table');
    let element_id;
    table.addEventListener('click', (event) => {
        const isButton = event.target.nodeName === 'BUTTON';
        if (!isButton) {
            return;
        }
        element_id = event.target.parentElement.parentElement.id;
        if (event.target.innerHTML === 'Edit') {
            $('#ProductTitle').val($('#element_ukrTitle_' + element_id).text());
            $('#changed_element_ukrTitle').val($('#element_ukrTitle_' + element_id).text());
            $('#changed_element_engTitle').val($('#element_engTitle_' + element_id).text());
            $('#changed_element_quantity').val($('#element_quantity_' + element_id).text());
            $('#changed_element_price').val($('#element_price_' + element_id).text());
            $('#changed_element_code').val($('#element_code_' + element_id).text());
            return;
        }
        fetch('http://localhost:8080/products?id=' + element_id, {
            method: 'DELETE',
        }).then(response => {
            if (response.status === 200) {
                event.target.parentElement.parentElement.remove()
            }
        });
    });

    document.getElementById('changedInfoButton').addEventListener('click', () => {
        let data = new FormData();
        data.append('id', element_id);
        data.append('new_code', $('#changed_element_code').val());
        data.append('new_price', $('#changed_element_price').val());
        data.append('new_title_ukr', $('#changed_element_ukrTitle').val());
        data.append('new_title_eng', $('#changed_element_engTitle').val());
        data.append('new_quantity', $('#changed_element_quantity').val());
        console.log("element_id = " + element_id);
        fetch('http://localhost:8080/products?id=' + element_id, {
            method: 'PUT',
            body: data
        }).then(response => {
            if (response.status === 200) {
                $(function () {
                    $('#exampleModal').modal('toggle')
                });
                $('#element_ukrTitle_' + element_id).text($('#changed_element_ukrTitle').val());
                $('#element_engTitle_' + element_id).text($('#changed_element_engTitle').val());
                $('#element_quantity_' + element_id).text($('#changed_element_quantity').val());
                $('#element_price_' + element_id).text($('#changed_element_price').val());
                $('#element_code_' + element_id).text($('#changed_element_code').val());
            }
        }).catch(err => console.log(err));
    });
</script>
</html>
