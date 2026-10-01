package com.example.web_session_w4.vnpay;

import java.io.Serializable;
import java.util.Date;

public class PendingPayment implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String txnRef;
    private final long expectedVnpAmount;
    private final double amountUsd;
    private final long amountVnd;
    private final String orderInfo;
    private final Date createdAt;

    public PendingPayment(String txnRef, long expectedVnpAmount, double amountUsd, long amountVnd, String orderInfo) {
        this.txnRef = txnRef;
        this.expectedVnpAmount = expectedVnpAmount;
        this.amountUsd = amountUsd;
        this.amountVnd = amountVnd;
        this.orderInfo = orderInfo;
        this.createdAt = new Date();
    }

    public String getTxnRef() {
        return txnRef;
    }

    public long getExpectedVnpAmount() {
        return expectedVnpAmount;
    }

    public double getAmountUsd() {
        return amountUsd;
    }

    public long getAmountVnd() {
        return amountVnd;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
