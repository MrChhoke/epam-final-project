<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="show"%>
<html>
<head>
    <title>Products</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/node_modules/bootstrap/dist/css/bootstrap.min.css">
    <script src="${pageContext.request.contextPath}/node_modules/jquery/dist/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/node_modules/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
    <jsp:useBean id="user" scope="session" type="com.epam.cash.register.entity.User"/>
    <jsp:useBean id="mapProductAtReceipt" scope="request" type="java.util.Map<java.lang.Long,java.lang.Long>"/>
</head>
<body>
<div class="container">
    <c:if test="${user.role == 'ADMIN' || user.role == 'COMMODITY_EXPERT'}">
        <form method="post" action="${pageContext.request.contextPath}/products">
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
    </c:if>
    <table id="table" class="table">
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Ukrainian Title</th>
            <th scope="col">English Title</th>
            <th scope="col">Price</th>
            <th scope="col">Quantity</th>
            <th scope="col">Code</th>
            <c:if test="${user.role == 'ADMIN' || user.role == 'COMMODITY_EXPERT'}">
                <th scope="col"></th>
            </c:if>

            <c:if test="${user.role == 'ADMIN' || user.role == 'COMMODITY_EXPERT'}">
                <th scope="col"></th>
            </c:if>
            <c:if test="${user.role == 'ADMIN' || user.role == 'CASHIER' || user.role == 'SENIOR_CASHIER'}">
                <th scope="col">Quanitity</th>
            </c:if>
            <c:if test="${user.role == 'ADMIN' || user.role == 'CASHIER' || user.role == 'SENIOR_CASHIER'}">
                <th scope="col"></th>
            </c:if>
        </tr>
        <jsp:useBean id="all_products" scope="request" type="java.util.List<com.epam.cash.register.entity.Product>"/>
        <c:forEach items="${all_products}" var="product">
            <tr id="${product.id}">
                <show:dispayProduct product="${product}"/>
                <c:if test="${user.role == 'ADMIN' || user.role == 'COMMODITY_EXPERT'}">
                    <td>
                        <button type="button" class="btn btn-danger">Delete</button>
                    </td>
                </c:if>
                <c:if test="${user.role == 'ADMIN' || user.role == 'COMMODITY_EXPERT'}">
                    <td>
                        <button type="button" class="btn btn-info" data-toggle="modal" data-target="#exampleModal">
                            Edit
                        </button>
                    </td>
                </c:if>
                <c:if test="${user.role == 'ADMIN' || user.role == 'CASHIER' || user.role == 'SENIOR_CASHIER'}">
                    <td><input type="number" class="form-control"
                               value="${mapProductAtReceipt.get(product.id) != null ? mapProductAtReceipt.get(product.id) : 0}"
                               id="input_element_quantity_${product.id}">
                    </td>
                </c:if>
                <c:if test="${user.role == 'ADMIN' || user.role == 'CASHIER' || user.role == 'SENIOR_CASHIER'}">
                    <td>
                        <button type="button" class="btn btn-success">Add to receipt</button>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
    </table>
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

        let buttonText = event.target.innerHTML.trim();

        if (buttonText === 'Edit') {
            $('#ProductTitle').val($('#element_ukrTitle_' + element_id).text());
            $('#changed_element_ukrTitle').val($('#element_ukrTitle_' + element_id).text());
            $('#changed_element_engTitle').val($('#element_engTitle_' + element_id).text());
            $('#changed_element_quantity').val($('#element_quantity_' + element_id).text());
            $('#changed_element_price').val($('#element_price_' + element_id).text());
            $('#changed_element_code').val($('#element_code_' + element_id).text());

        } else if (buttonText === 'Delete') {
            fetch('${pageContext.request.contextPath}?id=' + element_id, {
                    method: 'DELETE',
                }
            )
                .then(response => {
                        if (response.status === 200) {
                            event.target.parentElement.parentElement.remove()
                        }
                    }
                );
        } else if (buttonText === 'Add to receipt') {
            let data = new FormData();

            data.append('quantityProduct', $('#input_element_quantity_' + element_id).val());
            data.append('productID', element_id);

            fetch('${pageContext.request.contextPath}/receipt', {
                    method: 'POST',
                    body: data
                }
            )
                .then(res => console.log(res))
                .catch(err => console.log(err));
        }
    });


    document.getElementById('changedInfoButton').addEventListener('click', () => {
        let data = new FormData();

        data.append('id', element_id);
        data.append('new_code', $('#changed_element_code').val());
        data.append('new_price', $('#changed_element_price').val());
        data.append('new_title_ukr', $('#changed_element_ukrTitle').val());
        data.append('new_title_eng', $('#changed_element_engTitle').val());
        data.append('new_quantity', $('#changed_element_quantity').val());

        fetch('${pageContext.request.contextPath}products?id=' + element_id, {
                method: 'PUT',
                body: data
            }
        ).then(response => {
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
            }
        );
    });
</script>
</html>
