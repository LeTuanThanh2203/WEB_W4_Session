package com.example.web_session_w4.vnpay;

import com.example.web_session_w4.model.Cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "VnpayPaymentServlet", urlPatterns = {"/vnpay-payment"})
public class VnpayPaymentServlet extends HttpServlet {

    private final VnpayConfig config = new VnpayConfig();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // Server-side calculation of VND amount with BigDecimal rounding (HALF_UP, scale 0)
        BigDecimal totalUsd = BigDecimal.valueOf(cart.getTotal());
        BigDecimal exchangeRate = BigDecimal.valueOf(config.getExchangeRate());
        BigDecimal amountVndBg = totalUsd.multiply(exchangeRate).setScale(0, RoundingMode.HALF_UP);
        long amountVnd = amountVndBg.longValueExact();
        long vnpAmount = amountVnd * 100L;

        // VNPAY Techspec 2.1.0 minimum amount check: 5,000 VND (500,000 in vnp_Amount)
        if (amountVnd < 5000) {
            request.setAttribute("errorMessage", "Số tiền thanh toán tối thiểu VNPAY là 5,000 VND.");
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
            return;
        }

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        String createDate = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 15);
        String expireDate = formatter.format(cld.getTime());

        String txnRef = formatter.format(new Date()) + VnpayUtil.getRandomNumber(4);
        String orderInfo = "Thanh toan don hang " + txnRef;

        String returnUrl = config.getReturnUrl();
        if (returnUrl == null || returnUrl.trim().isEmpty()) {
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            String contextPath = request.getContextPath();

            String portStr = ((scheme.equals("http") && serverPort == 80) || (scheme.equals("https") && serverPort == 443))
                    ? "" : ":" + serverPort;
            returnUrl = scheme + "://" + serverName + portStr + contextPath + "/vnpay-return";
        }

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", config.getTmnCode());
        vnpParams.put("vnp_Amount", String.valueOf(vnpAmount));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", txnRef);
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_IpAddr", VnpayUtil.getIpAddress(request));
        vnpParams.put("vnp_CreateDate", createDate);
        vnpParams.put("vnp_ExpireDate", expireDate);

        // Store pending payment in session
        PendingPayment pending = new PendingPayment(txnRef, vnpAmount, cart.getTotal(), amountVnd, orderInfo);
        session.setAttribute("pendingPayment", pending);

        // Build hash and payment URL
        String secureHash = VnpayUtil.hashAllFields(vnpParams, config.getHashSecret());
        vnpParams.put("vnp_SecureHash", secureHash);

        String paymentUrl = config.getPaymentUrl() + "?" + VnpayUtil.buildQueryString(vnpParams);
        response.sendRedirect(paymentUrl);
    }
}
