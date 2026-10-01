package com.example.web_session_w4.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Model representing a LineItem in the shopping cart, wrapping a Product and quantity.
 */
public class LineItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Product product;
    private int quantity;

    public LineItem() {
        this.product = new Product();
        this.quantity = 0;
    }

    public LineItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return product.getPrice() * quantity;
    }

    public String getTotalCurrencyFormat() {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        return currency.format(getTotal());
    }

    // Convenience aliases for JSP EL accessibility
    public String getProductCode() {
        return product != null ? product.getCode() : "";
    }

    public String getProductId() {
        return getProductCode();
    }

    public String getProductName() {
        return product != null ? product.getDescription() : "";
    }

    public String getDescription() {
        return getProductName();
    }

    public String getPriceCurrencyFormat() {
        return product != null ? product.getPriceCurrencyFormat() : "$0.00";
    }
}
