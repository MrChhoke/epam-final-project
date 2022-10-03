<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute
        name="product"
        required="true"
        type="com.epam.cash.register.entity.Product"
%>

<td scope="row">${product.id}</td>
<td id="element_ukrTitle_${product.id}">${product.title_ukr}</td>
<td id="element_engTitle_${product.id}">${product.title_eng}</td>
<td id="element_price_${product.id}">${product.price}</td>
<td id="element_quantity_${product.id}">${product.quantity}</td>
<td id="element_code_${product.id}">${product.code}</td>
