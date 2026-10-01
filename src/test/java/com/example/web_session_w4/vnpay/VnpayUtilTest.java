package com.example.web_session_w4.vnpay;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class VnpayUtilTest {

    @Test
    public void testHmacSHA512() {
        String key = "SECRETKEY123";
        String data = "vnp_Amount=1000000&vnp_Command=pay&vnp_TmnCode=DEMO";
        String hash = VnpayUtil.hmacSHA512(key, data);
        assertNotNull(hash);
        assertEquals(128, hash.length()); // SHA-512 hex string length is 128 characters
        assertEquals(hash.toLowerCase(), hash); // Must be lowercase hex
    }

    @Test
    public void testHashAllFieldsSorting() {
        Map<String, String> fields = new HashMap<>();
        fields.put("vnp_Command", "pay");
        fields.put("vnp_Amount", "1000000");
        fields.put("vnp_TmnCode", "DEMO");

        String hash = VnpayUtil.hashAllFields(fields, "SECRETKEY123");
        assertNotNull(hash);
        assertEquals(128, hash.length());
    }

    @Test
    public void testAmountCalculationBigDecimal() {
        double totalUsd = 14.95;
        double exchangeRate = 25000.0;

        BigDecimal totalUsdBg = BigDecimal.valueOf(totalUsd);
        BigDecimal exchangeRateBg = BigDecimal.valueOf(exchangeRate);
        BigDecimal amountVndBg = totalUsdBg.multiply(exchangeRateBg).setScale(0, RoundingMode.HALF_UP);
        long amountVnd = amountVndBg.longValueExact();
        long vnpAmount = amountVnd * 100L;

        assertEquals(373750L, amountVnd); // 14.95 * 25000 = 373750
        assertEquals(37375000L, vnpAmount);
    }
}
