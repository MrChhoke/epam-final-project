<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Receipt</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/node_modules/bootstrap/dist/css/bootstrap.min.css">
    <script src="${pageContext.request.contextPath}/node_modules/jquery/dist/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/node_modules/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="container">
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
            <tr id="${receipt.receiptCode}">
                <th scope="row">${receipt.id}</th>
                <th>${receipt.receiptCode}</th>
                <th>${receipt.totalPrice}</th>
                <th>${receipt.userCreator.username}</th>
                <th>${receipt.isDone()}</th>
                <th>${receipt.isCanceled()}</th>
                <th>${receipt.userCanceler.username}</th>
                <th>
                    <button class="btn btn-primary" type="button" data-toggle="collapse"
                            onclick="document.getElementById('${receipt.id}').hidden = !document.getElementById('${receipt.getId()}').hidden"
                            data-target="#collapseReceipt${receipt.id}"
                            aria-expanded="false" aria-controls="collapseExample">
                        Show
                    </button>
                </th>
                <th>
                    <c:if test="${!receipt.isDone()}">
                        <button class="btn btn-success" type="button">Accept</button>
                    </c:if>
                </th>
            </tr>
            <tr hidden id="${receipt.id}">
                <th colspan="8">
                    <div class="collapse" id="collapseReceipt${receipt.getId()}">
                        <div class="card card-body">
                            <table class="table">
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
                                <c:forEach items="${receipt.getItems()}" var="itemReceipt">
                                    <tr>
                                        <th scope="row">${itemReceipt.id}</th>
                                        <th>${itemReceipt.product.title_ukr}</th>
                                        <th>${itemReceipt.product.title_eng}</th>
                                        <th>${itemReceipt.product.price}</th>
                                        <th>${itemReceipt.quantity}</th>
                                        <th>${itemReceipt.product.code}</th>
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
            data.append('receipt_code', code_receipt.trim());
            data.append('done', 'done');
            fetch('${pageContext.request.contextPath}/receipt', {
                    method: 'POST',
                    body: data
                }
            ).then(response => {
                if (response.status === 200) {
                    event.target.parentElement.parentElement.children[4].innerHTML = 'true';
                    event.target.remove();
                }
            })
        }

    });


</script>
</html>
