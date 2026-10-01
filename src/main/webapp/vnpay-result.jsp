<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Kết quả thanh toán VNPAY</title>
    <link rel="stylesheet" href="<c:url value='/styles/main.css'/>" type="text/css">
    <style>
        .result-card {
            max-width: 600px;
            margin: 40px auto;
            padding: 30px;
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
            font-family: Arial, sans-serif;
        }
        .result-header {
            text-align: center;
            margin-bottom: 25px;
            padding-bottom: 15px;
            border-bottom: 2px solid #f0f0f0;
        }
        .status-badge {
            display: inline-block;
            padding: 8px 18px;
            font-weight: bold;
            border-radius: 20px;
            font-size: 16px;
            margin-bottom: 10px;
        }
        .status-success {
            background-color: #e6f4ea;
            color: #137333;
        }
        .status-failed {
            background-color: #fce8e6;
            color: #c5221f;
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px dashed #e0e0e0;
        }
        .info-label {
            font-weight: bold;
            color: #555;
        }
        .info-value {
            color: #222;
        }
        .btn-container {
            margin-top: 30px;
            text-align: center;
        }
        .btn-action {
            display: inline-block;
            padding: 10px 20px;
            background-color: #ee4d2d;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-weight: bold;
            margin: 0 5px;
        }
        .btn-action:hover {
            background-color: #d73211;
        }
    </style>
</head>
<body>

    <div class="result-card">
        <div class="result-header">
            <c:choose>
                <c:when test="${paymentResult.success}">
                    <div class="status-badge status-success">THANH TOÁN THÀNH CÔNG</div>
                </c:when>
                <c:otherwise>
                    <div class="status-badge status-failed">THANH TOÁN THẤT BẠI / CẢNH BÁO</div>
                </c:otherwise>
            </c:choose>
            <h2><c:out value="${paymentResult.message}"/></h2>
        </div>

        <div class="info-row">
            <span class="info-label">Mã giao dịch (TxnRef):</span>
            <span class="info-value"><strong><c:out value="${paymentResult.txnRef}"/></strong></span>
        </div>

        <div class="info-row">
            <span class="info-label">Số tiền (VND):</span>
            <span class="info-value" style="color: #ee4d2d; font-weight: bold;">
                <fmt:formatNumber value="${paymentResult.amountVnd}" type="currency" currencySymbol="VND " pattern="#,##0 ¤"/>
            </span>
        </div>

        <div class="info-row">
            <span class="info-label">Mã phản hồi (Response Code):</span>
            <span class="info-value"><c:out value="${paymentResult.responseCode}"/></span>
        </div>

        <div class="info-row">
            <span class="info-label">Thông tin đơn hàng:</span>
            <span class="info-value"><c:out value="${paymentResult.orderInfo}"/></span>
        </div>

        <c:if test="${paymentResult.sessionMissing}">
            <div style="margin-top: 15px; padding: 10px; background-color: #fff3cd; color: #856404; border-radius: 5px; font-size: 13px;">
                <strong>Lưu ý:</strong> Phiên làm việc (Session) đã hết hạn hoặc không khớp. Giỏ hàng của bạn được giữ nguyên.
            </div>
        </c:if>

        <div class="btn-container">
            <a href="<c:url value='/cart'/>" class="btn-action">Quay lại Giỏ hàng</a>
            <a href="<c:url value='/index.jsp'/>" class="btn-action" style="background-color: #6c757d;">Tiếp tục mua sắm</a>
        </div>
    </div>

</body>
</html>
