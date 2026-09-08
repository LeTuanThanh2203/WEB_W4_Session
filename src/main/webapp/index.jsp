
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" type="text/css" href="styles/main.css">
</head>
<body>

    <h2>CD list</h2>

    <table>
        <thead>
            <tr>
                <th>Description</th>
                <th>Price</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>86 (the band) - True Life Songs and Pictures</td>
                <td>$14.95</td>
                <td>
                    <form action="${pageContext.request.contextPath}/cart?action=add" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="8601">
                        <input type="hidden" name="productCode" value="8601">
                        <input type="hidden" name="productName" value="86 (the band) - True Life Songs and Pictures">
                        <input type="hidden" name="price" value="14.95">
                        <input type="hidden" name="quantity" value="1">
                        <input type="submit" value="Add To Cart">
                    </form>
                </td>
            </tr>
            <tr>
                <td>Paddlefoot - The first CD</td>
                <td>$12.95</td>
                <td>
                    <form action="${pageContext.request.contextPath}/cart?action=add" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="pf01">
                        <input type="hidden" name="productCode" value="pf01">
                        <input type="hidden" name="productName" value="Paddlefoot - The first CD">
                        <input type="hidden" name="price" value="12.95">
                        <input type="hidden" name="quantity" value="1">
                        <input type="submit" value="Add To Cart">
                    </form>
                </td>
            </tr>
            <tr>
                <td>Paddlefoot - The second CD</td>
                <td>$14.95</td>
                <td>
                    <form action="${pageContext.request.contextPath}/cart?action=add" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="pf02">
                        <input type="hidden" name="productCode" value="pf02">
                        <input type="hidden" name="productName" value="Paddlefoot - The second CD">
                        <input type="hidden" name="price" value="14.95">
                        <input type="hidden" name="quantity" value="1">
                        <input type="submit" value="Add To Cart">
                    </form>
                </td>
            </tr>
            <tr>
                <td>Joe Rut - Genuine Wood Grained Finish</td>
                <td>$14.95</td>
                <td>
                    <form action="${pageContext.request.contextPath}/cart?action=add" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="jr01">
                        <input type="hidden" name="productCode" value="jr01">
                        <input type="hidden" name="productName" value="Joe Rut - Genuine Wood Grained Finish">
                        <input type="hidden" name="price" value="14.95">
                        <input type="hidden" name="quantity" value="1">
                        <input type="submit" value="Add To Cart">
                    </form>
                </td>
            </tr>
        </tbody>
    </table>

</body>
</html>