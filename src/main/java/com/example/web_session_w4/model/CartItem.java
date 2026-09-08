package com.example.web_session_w4.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Model class representing an item in the shopping cart.
 */
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productId;
    private String productName;
    private double price;
    private int quantity;

    public CartItem() {
        this.productId = "";
        this.productName = "";
        this.price = 0.0;
        this.quantity = 0;
    }

    public CartItem(String productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    // Alias getter for compatibility with JSP forms using productCode
    public String getProductCode() {
        return productId;
    }

    public void setProductCode(String productCode) {
        this.productId = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Alias getter for compatibility with JSP accessing description
    public String getDescription() {
        return productName;
    }

    public void setDescription(String description) {
        this.productName = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculates the subtotal for this item (price * quantity).
     */
    public double getTotal() {
        return price * quantity;
    }

    public double getAmount() {
        return getTotal();
    }

    /**
     * Formatted price (e.g. $14.95)
     */
    public String getPriceCurrencyFormat() {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        return currency.format(price);
    }

    /**
     * Formatted subtotal amount (e.g. $14.95)
     */
    public String getTotalCurrencyFormat() {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        return currency.format(getTotal());
    }
}
