package com.example.web_session_w4.vnpay;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VnpayConfig {
    private static final String DEFAULT_PAYMENT_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String DEFAULT_TMN_CODE = "YOUR_TMN_CODE";
    private static final String DEFAULT_HASH_SECRET = "YOUR_HASH_SECRET";
    private static final double DEFAULT_EXCHANGE_RATE = 25000.0;

    private final String tmnCode;
    private final String hashSecret;
    private final String paymentUrl;
    private final String returnUrl;
    private final double exchangeRate;

    public VnpayConfig() {
        Properties fileProps = new Properties();
        try (InputStream is = VnpayConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                fileProps.load(is);
            }
        } catch (IOException ignored) {
        }

        this.tmnCode = resolveConfig("VNP_TMNCODE", "vnp.tmncode", fileProps.getProperty("vnp_TmnCode"), DEFAULT_TMN_CODE);
        this.hashSecret = resolveConfig("VNP_HASHSECRET", "vnp.hashsecret", fileProps.getProperty("vnp_HashSecret"), DEFAULT_HASH_SECRET);
        this.paymentUrl = resolveConfig("VNP_PAYMENT_URL", "vnp.url", fileProps.getProperty("vnp_Url"), DEFAULT_PAYMENT_URL);
        this.returnUrl = resolveConfig("VNP_RETURN_URL", "vnp.returnurl", fileProps.getProperty("vnp_ReturnUrl"), null);

        String rateStr = resolveConfig("VNP_EXCHANGE_RATE", "vnp.exchangerate", fileProps.getProperty("vnp_ExchangeRate"), String.valueOf(DEFAULT_EXCHANGE_RATE));
        double parsedRate = DEFAULT_EXCHANGE_RATE;
        try {
            parsedRate = Double.parseDouble(rateStr);
        } catch (Exception ignored) {
        }
        this.exchangeRate = parsedRate;
    }

    private String resolveConfig(String envKey, String sysPropKey, String fileVal, String defaultVal) {
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.trim().isEmpty()) {
            return envVal.trim();
        }
        String sysVal = System.getProperty(sysPropKey);
        if (sysVal != null && !sysVal.trim().isEmpty()) {
            return sysVal.trim();
        }
        if (fileVal != null && !fileVal.trim().isEmpty()) {
            return fileVal.trim();
        }
        return defaultVal;
    }

    public String getTmnCode() {
        return tmnCode;
    }

    public String getHashSecret() {
        return hashSecret;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }
}
