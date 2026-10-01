package com.example.web_session_w4.vnpay;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "VnpayReturnServlet", urlPatterns = {"/vnpay-return"})
public class VnpayReturnServlet extends HttpServlet {

    private final VnpayConfig config = new VnpayConfig();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processReturn(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processReturn(request, response);
    }

    private void processReturn(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String txnRef = request.getParameter("vnp_TxnRef");

        // F5 Refresh Handling: If this transaction was already processed and cached in session, render previous result
        PaymentResult lastResult = (PaymentResult) session.getAttribute("lastPaymentResult");
        if (lastResult != null && txnRef != null && txnRef.equals(lastResult.getTxnRef())) {
            request.setAttribute("paymentResult", lastResult);
            request.getRequestDispatcher("/vnpay-result.jsp").forward(request, response);
            return;
        }

        // Collect all vnp_ parameters
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            if (paramValue != null && !paramValue.trim().isEmpty()) {
                fields.put(paramName, paramValue.trim());
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        // Verify checksum
        String calculatedHash = VnpayUtil.hashAllFields(fields, config.getHashSecret());
        boolean isValidSignature = VnpayUtil.constantTimeEquals(calculatedHash, vnp_SecureHash);

        if (!isValidSignature) {
            PaymentResult result = new PaymentResult(
                    false,
                    "Dữ liệu không hợp lệ (Chữ ký không khớp).",
                    txnRef != null ? txnRef : "N/A",
                    0,
                    request.getParameter("vnp_ResponseCode"),
                    request.getParameter("vnp_OrderInfo")
            );
            session.setAttribute("lastPaymentResult", result);
            request.setAttribute("paymentResult", result);
            request.getRequestDispatcher("/vnpay-result.jsp").forward(request, response);
            return;
        }

        String responseCode = request.getParameter("vnp_ResponseCode");
        String vnpAmountStr = request.getParameter("vnp_Amount");
        String orderInfo = request.getParameter("vnp_OrderInfo");
        long rawVnpAmount = 0;
        try {
            if (vnpAmountStr != null) {
                rawVnpAmount = Long.parseLong(vnpAmountStr);
            }
        } catch (NumberFormatException ignored) {
        }
        long amountVnd = rawVnpAmount / 100;

        PendingPayment pending = (PendingPayment) session.getAttribute("pendingPayment");

        PaymentResult result;
        if (pending != null && txnRef != null && txnRef.equals(pending.getTxnRef())) {
            if (rawVnpAmount == pending.getExpectedVnpAmount()) {
                if ("00".equals(responseCode)) {
                    // Success: Clear Cart & Clear Pending Payment
                    session.removeAttribute("cart");
                    session.removeAttribute("pendingPayment");

                    result = new PaymentResult(
                            true,
                            "Giao dịch thanh toán VNPAY thành công!",
                            txnRef,
                            amountVnd,
                            responseCode,
                            pending.getOrderInfo()
                    );
                } else {
                    // Cancelled / Failed: Keep Cart intact, clear Pending Payment
                    session.removeAttribute("pendingPayment");

                    result = new PaymentResult(
                            false,
                            "Giao dịch không thành công hoặc bị hủy (Mã lỗi: " + responseCode + ").",
                            txnRef,
                            amountVnd,
                            responseCode,
                            pending.getOrderInfo()
                    );
                }
            } else {
                result = new PaymentResult(
                        false,
                        "Số tiền thanh toán từ VNPAY không khớp với dữ liệu đơn hàng.",
                        txnRef,
                        amountVnd,
                        responseCode,
                        pending.getOrderInfo()
                );
            }
        } else {
            // Missing pending session state (Session lost/expired)
            if ("00".equals(responseCode)) {
                result = new PaymentResult(
                        false,
                        "Thanh toán VNPAY phản hồi thành công, nhưng không thể đối chiếu với phiên làm việc (Session đã hết hạn). Giỏ hàng chưa được xóa tự động.",
                        txnRef,
                        amountVnd,
                        responseCode,
                        orderInfo,
                        true
                );
            } else {
                result = new PaymentResult(
                        false,
                        "Giao dịch không thành công và không đối chiếu được phiên làm việc.",
                        txnRef,
                        amountVnd,
                        responseCode,
                        orderInfo,
                        true
                );
            }
        }

        session.setAttribute("lastPaymentResult", result);
        request.setAttribute("paymentResult", result);
        request.getRequestDispatcher("/vnpay-result.jsp").forward(request, response);
    }
}
