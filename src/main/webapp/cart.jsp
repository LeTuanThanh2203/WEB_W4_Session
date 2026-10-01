<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="utf-8">
                <title>Murach's Java Servlets and JSP</title>
                <link rel="stylesheet" href="<c:url value='/styles/main.css'/>" type="text/css">
            </head>

            <body>

                <div class="cart-container-3d">
                    <h1>Your cart</h1>

                    <c:choose>
                        <c:when test="${empty cart || empty cart.items}">
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
                                    <c:forEach var="item" items="${cart.items}">
                                        <tr>
                                            <td class="nowrap">
                                                <form action="<c:url value='/cart'/>" method="post">
                                                    <input type="hidden" name="action" value="update">
                                                    <input type="hidden" name="productCode"
                                                        value="<c:out value='${item.product.code}'/>">
                                                    <input type="text" size="2" name="quantity"
                                                        value="<c:out value='${item.quantity}'/>">
                                                    <input type="submit" value="Update">
                                                </form>
                                            </td>
                                            <td>
                                                <c:out value="${item.product.description}" />
                                            </td>
                                            <td class="right">
                                                <c:out value="${item.product.priceCurrencyFormat}" />
                                            </td>
                                            <td class="right">
                                                <c:out value="${item.totalCurrencyFormat}" />
                                            </td>
                                            <td>
                                                <form action="<c:url value='/cart'/>" method="post">
                                                    <input type="hidden" name="action" value="remove">
                                                    <input type="hidden" name="productCode"
                                                        value="<c:out value='${item.product.code}'/>">
                                                    <input type="submit" value="Remove Item">
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <tr>
                                        <td colspan="3" style="text-align: right; font-weight: bold;">Total:</td>
                                        <td class="right"
                                            style="font-weight: bold; color: var(--shopee-orange); font-size: 16px;">
                                            <c:out value="${cart.totalCurrencyFormat}" />
                                        </td>
                                        <td></td>
                                    </tr>
                                </tbody>
                            </table>

                            <p><b>To change the quantity</b>, enter the new quantity and click on the Update button.</p>
                        </c:otherwise>
                    </c:choose>

                    <div style="margin-top: 15px;">
                        <form action="<c:url value='/cart'/>" method="post" style="display: inline-block;">
                            <input type="hidden" name="action" value="shop">
                            <input type="submit" value="Continue Shopping">
                        </form>

                        <form action="<c:url value='/cart'/>" method="post" style="display: inline-block;">
                            <input type="hidden" name="action" value="checkout">
                            <input type="submit" value="Checkout">
                        </form>

                        <c:if test="${not empty cart && not empty cart.items}">
                            <form action="<c:url value='/vnpay-payment'/>" method="post" style="display: inline-block; margin-left: 10px;">
                                <input type="submit" value="Thanh toán bằng VNPAY" style="background-color: #0056b3; color: white; border: none; padding: 8px 16px; border-radius: 4px; font-weight: bold; cursor: pointer;">
                            </form>
                        </c:if>
                    </div>
                </div>

            </body>

            </html>