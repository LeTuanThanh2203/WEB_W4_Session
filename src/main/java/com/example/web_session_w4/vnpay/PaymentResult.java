package com.example.web_session_w4.vnpay;

import java.io.Serializable;

public class PaymentResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;
    private final String txnRef;
    private final long amountVnd;
    private final String responseCode;
    private final String orderInfo;
    private final boolean sessionMissing;

    public PaymentResult(boolean success, String message, String txnRef, long amountVnd, String responseCode, String orderInfo) {
        this(success, message, txnRef, amountVnd, responseCode, orderInfo, false);
    }

    public PaymentResult(boolean success, String message, String txnRef, long amountVnd, String responseCode, String orderInfo, boolean sessionMissing) {
        this.success = success;
        this.message = message;
        this.txnRef = txnRef;
        this.amountVnd = amountVnd;
        this.responseCode = responseCode;
        this.orderInfo = orderInfo;
        this.sessionMissing = sessionMissing;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getTxnRef() {
        return txnRef;
    }

    public long getAmountVnd() {
        return amountVnd;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public boolean isSessionMissing() {
        return sessionMissing;
    }
}
