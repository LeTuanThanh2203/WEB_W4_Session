<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="styles/main.css" type="text/css">
</head>
<body>

<h1>Your cart</h1>

<c:choose>
    <c:when test="${empty sessionScope.cart || empty sessionScope.cart.items}">
        <p>Your cart is currently empty.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
                <tr>
                    <th>Quantity</th>
                    <th>Description</th>
                    <th class="right">Price</th>
                    <th class="right">Amount</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${sessionScope.cart.items}">
                    <tr>
                        <td class="nowrap">
                            <form action="${pageContext.request.contextPath}/quantity" method="post">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="productId" value="${item.productId}">
                                <input type="hidden" name="productCode" value="${item.productCode}">
                                <input type="text" size="2" name="quantity" value="${item.quantity}">
                                <input type="submit" value="Update">
                            </form>
                        </td>
                        <td>${item.productName}</td>
                        <td class="right">${item.priceCurrencyFormat}</td>
                        <td class="right">${item.totalCurrencyFormat}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/quantity" method="post">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="productId" value="${item.productId}">
                                <input type="hidden" name="productCode" value="${item.productCode}">
                                <input type="submit" value="Remove Item">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="3" style="text-align: right; font-weight: bold;">Total:</td>
                    <td class="right" style="font-weight: bold;">${sessionScope.cart.totalCurrencyFormat}</td>
                    <td></td>
                </tr>
            </tbody>
        </table>

        <p><b>To change the quantity</b>, enter the new quantity and click on the Update button.</p>
    </c:otherwise>
</c:choose>

<form action="${pageContext.request.contextPath}/cart" method="post">
    <input type="hidden" name="action" value="shop">
    <input type="submit" value="Continue Shopping">
</form>

<form action="${pageContext.request.contextPath}/cart" method="post">
    <input type="hidden" name="action" value="checkout">
    <input type="submit" value="Checkout">
</form>

</body>
</html>
