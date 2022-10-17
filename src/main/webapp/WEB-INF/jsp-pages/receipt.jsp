<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://mytags.com" %>
<html>
<head>
    <title>Receipt</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/node_modules/bootstrap/dist/css/bootstrap.min.css">
    <script src="${pageContext.request.contextPath}/node_modules/jquery/dist/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/node_modules/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/node_modules/bootstrap-icons/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/sidebar.css">
    <jsp:useBean id="user" scope="session" class="com.epam.cash.register.entity.User"/>
    <jsp:useBean id="receipts" scope="request" type="java.util.List<com.epam.cash.register.entity.Receipt>"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-3 px-1 bg-dark position-fixed" id="sticky-sidebar">
            <div class="wrapper d-flex">
                <div class="sidebar">
                    <small class="text-muted pl-3">SEARCH</small>
                    <ul>
                        <li class="search">
                            <div class="d-flex justify-content-center">
                                <div class="input-group mb-3" style="width: 90%">
                                    <input type="text" class="form-control" placeholder="Creator`s name"
                                           id="inputFilterUserCreator">
                                    <div class="input-group-append">
                                        <button class="btn btn-secondary" type="button"
                                                onclick="findReceiptsByCreatorName()"><i class="bi bi-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li class="search">
                            <div class="d-flex justify-content-center">
                                <div class="input-group mb-3" style="width: 90%">
                                    <input type="text" class="form-control" placeholder="Receipt`s code"
                                           id="inputFilterReceiptCode">
                                    <div class="input-group-append">
                                        <button class="btn btn-secondary" type="button"
                                                onclick="findReceiptsByReceiptCode()"><i class="bi bi-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li class="search">
                            <div class="d-flex justify-content-center">
                                <div class="input-group mb-3" style="width: 90%">
                                    <input type="text" class="form-control" placeholder="Canceler`s name"
                                           id="inputFilterUserCanceler">
                                    <div class="input-group-append">
                                        <button class="btn btn-secondary" type="button"
                                                onclick="findReceiptsByCancelerName()"><i class="bi bi-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </ul>
                    <small class="text-muted px-3">Filter</small>
                    <ul>
                        <li class="search">
                            <div class="d-flex justify-content-center">
                                <div class="input-group mb-3" style="width: 90%">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text">Min price: </span>
                                    </div>
                                    <input type="number" class="form-control" id="minPrice_Filtering">
                                </div>
                            </div>
                            <div class="d-flex justify-content-center">
                                <div class="input-group mb-3" style="width: 90%">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text">Max price: </span>
                                    </div>
                                    <input type="number" class="form-control" id="maxPrice_Filtering">
                                </div>
                            </div>
                        </li>
                        <li class="search">
                            <div class="d-flex justify-content-center">
                                <div class="input-group mb-3" style="width: 90%; color: #f3f5f9">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text">Canceled</span>
                                    </div>
                                    <select id="isCanceled_Filtering" class="form-control">
                                        <option>ANY</option>
                                        <option>TRUE</option>
                                        <option>FALSE</option>
                                    </select>
                                </div>
                            </div>
                        </li>
                        <li class="search">
                            <div class="d-flex justify-content-center">
                                <div class="input-group mb-3" style="width: 90%; color: #f3f5f9">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text">Done</span>
                                    </div>
                                    <select id="isDone_Filtering" class="form-control">
                                        <option>ANY</option>
                                        <option>TRUE</option>
                                        <option>FALSE</option>
                                    </select>
                                </div>
                            </div>
                        </li>
                        <li class="search">
                            <div class="d-flex justify-content-center">
                                <button class="btn btn-success" style="width: 90%" id="filteringButton">Filter</button>
                            </div>
                        </li>
                    </ul>
                    <small class="text-muted px-3">NAVIGATION</small>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/"><i class="fas fa-external-link-alt"></i>Main page</a></li>
                        <li><a href="${pageContext.request.contextPath}/products"><i class="fas fa-code"></i>Products</a></li>
                        <li><a href="${pageContext.request.contextPath}/receipt"><i class="fas fa-code"></i>Receipt</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="col offset-3" id="main">
            <table class="table" id="table">
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">ReceiptCode</th>
                    <th scope="col">Price</th>
                    <th scope="col">Creator</th>
                    <th scope="col">Done</th>
                    <th scope="col">Canceled</th>
                    <th scope="col">Canceler</th>
                    <th></th>
                    <th></th>
                </tr>
                <c:forEach var="receipt" items="${receipts}">
                    <tr id="${receipt.receiptCode}"
                        class="${receipt.canceled ? 'table-danger' : receipt.done ? 'table-success' : 'table-primary'}">
                        <s:displayReceipt receipt="${receipt}"/>
                        <td>
                            <button class="btn btn-primary" type="button" data-toggle="collapse"
                                    onclick="document.getElementById('${receipt.id}').hidden = !document.getElementById('${receipt.id}').hidden"
                                    data-target="#collapseReceipt${receipt.id}"
                                    aria-expanded="false" aria-controls="collapseExample">
                                Show
                            </button>
                        </td>
                        <td>
                            <c:if test="${(user.role == 'ADMIN' || user.role == 'CASHIER' || user.role == 'SENIOR_CASHIER') && (!receipt.done)}">
                                <button class="btn btn-success" type="button">Accept</button>
                            </c:if>
                            <c:if test="${(user.role == 'ADMIN' || user.role == 'SENIOR_CASHIER') && (!receipt.canceled && receipt.done)}">
                                <button class="btn btn-danger" type="button">Cancel</button>
                            </c:if>
                        </td>
                    </tr>
                    <tr hidden id="${receipt.id}">
                        <th colspan="8">
                            <div class="collapse" id="collapseReceipt${receipt.id}">
                                <div class="card card-body">
                                    <table class="table">
                                        <tr>
                                            <th scope="col">Ukrainian Title</th>
                                            <th scope="col">English Title</th>
                                            <th scope="col">Price</th>
                                            <th scope="col">Quantity</th>
                                            <th scope="col">Code</th>
                                            <th scope="col"></th>
                                        </tr>
                                        <c:forEach items="${receipt.items}" var="itemReceipt">
                                            <tr>
                                                <td>${itemReceipt.product.title_ukr}</td>
                                                <td>${itemReceipt.product.title_eng}</td>
                                                <td>${itemReceipt.product.price}</td>
                                                <td>${itemReceipt.quantity}</td>
                                                <td>${itemReceipt.product.code}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>
                        </th>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

</body>
<script>
    let table = document.getElementById('table');

    table.addEventListener('click', event => {
        const isButton = event.target.nodeName === 'BUTTON';
        if (!isButton) {
            return;
        }

        let code_receipt = event.target.parentElement.parentElement.id;

        let buttonText = event.target.innerHTML.trim();

        if (buttonText === 'Accept') {
            let data = new FormData();

            data.append('receiptCode', code_receipt.trim());
            data.append('done', 'done');

            fetch('${pageContext.request.contextPath}/receipt', {
                    method: 'POST',
                    body: data
                }
            ).then(response => {
                if (response.status === 200) {
                    let tableRow = event.target.parentElement.parentElement;
                    let role = '${user.role}';

                    tableRow.classList.remove('table-primary');
                    tableRow.classList.add('table-success');
                    tableRow.children[4].innerHTML = 'true';

                    if (role === 'ADMIN' || role === 'SENIOR_CASHIER') {
                        tableRow.children[8].innerHTML = '<button class="btn btn-danger" type="button">Cancel</button>'
                    }
                    event.target.remove();
                }
            })
        } else if (buttonText === 'Cancel') {
            let data = new FormData();

            data.append('canceledReceiptCode', code_receipt);

            fetch('${pageContext.request.contextPath}/receipt', {
                method: 'DELETE',
                body: data
            }).then(response => {
                if (response.status === 200) {
                    let tableRow = event.target.parentElement.parentElement;
                    let userName = '${user.username}';

                    tableRow.classList.remove('table-success');
                    tableRow.classList.add('table-danger');
                    tableRow.children[5].innerHTML = 'true';
                    tableRow.children[6].innerHTML = userName;

                    event.target.remove();
                }
            })
        }
    });

    document.getElementById('filteringButton').addEventListener('click', event => {
        const isButton = event.target.nodeName === 'BUTTON';
        if (!isButton) {
            return;
        }

        let params = "";

        let minPrice = $('#minPrice_Filtering').val();
        if (minPrice.length !== 0) {
            params += '&minPrice=' + $('#minPrice_Filtering').val();
        }

        let maxPrice = $('#maxPrice_Filtering').val();
        if (maxPrice.length !== 0) {
            params += '&maxPrice=' + $('#maxPrice_Filtering').val();
        }

        let isCanceled = $('#isCanceled_Filtering option:selected').text();
        if (isCanceled !== 'ANY') {
            params += '&isCanceled=' + (isCanceled === 'TRUE');
        }

        let isDone = $('#isDone_Filtering option:selected').text();
        if (isDone !== 'ANY') {
            params += '&isDone=' + (isDone === 'TRUE');
        }

        console.log(params);

        window.location.replace('${pageContext.request.contextPath}?filtering=true' + params);
    });

    function findReceiptsByCreatorName() {
        let params = "&creatorName=";
        params += $('#inputFilterUserCreator').val();
        window.location.replace('${pageContext.request.contextPath}?filtering=true' + params);
    }

    function findReceiptsByReceiptCode() {
        let params = "&receiptCode=";
        params += $('#inputFilterReceiptCode').val();
        window.location.replace('${pageContext.request.contextPath}?filtering=true' + params);
    }

    function findReceiptsByCancelerName() {
        let params = "&cancelerName=";
        params += $('#inputFilterReceiptCode').val();
        window.location.replace('${pageContext.request.contextPath}?filtering=true' + params);
    }

</script>
</html>
