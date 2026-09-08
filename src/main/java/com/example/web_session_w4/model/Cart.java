package com.example.web_session_w4.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Model class representing the shopping cart, containing a collection of CartItems.
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Adds an item to the cart.
     * If the item already exists in the cart, its quantity is increased.
     */
    public void addItem(CartItem item) {
        String code = item.getProductId();
        int quantity = item.getQuantity();

        for (CartItem existingItem : items) {
            if (existingItem.getProductId().equalsIgnoreCase(code)) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                if ((existingItem.getProductName() == null || existingItem.getProductName().isEmpty()) && item.getProductName() != null) {
                    existingItem.setProductName(item.getProductName());
                }
                if (existingItem.getPrice() <= 0 && item.getPrice() > 0) {
                    existingItem.setPrice(item.getPrice());
                }
                return;
            }
        }
        items.add(item);
    }

    /**
     * Updates the quantity of a product in the cart.
     * If quantity is <= 0, the item is removed from the cart.
     */
    public void updateQuantity(String productId, int quantity) {
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            if (item.getProductId().equalsIgnoreCase(productId)) {
                if (quantity <= 0) {
                    items.remove(i);
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
    }

    /**
     * Removes an item from the cart by its product ID.
     */
    public void removeItem(String productId) {
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            if (item.getProductId().equalsIgnoreCase(productId)) {
                items.remove(i);
                return;
            }
        }
    }

    /**
     * Calculates the total price of all items in the cart.
     */
    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotal();
        }
        return total;
    }

    /**
     * Formatted total amount (e.g. $29.90)
     */
    public String getTotalCurrencyFormat() {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        return currency.format(getTotal());
    }
}
