package com.example.web_session_w4.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Model Cart quản lý danh sách LineItem theo kiểu đối tượng Java cơ bản.
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<LineItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<LineItem> getItems() {
        return items;
    }

    public void setItems(List<LineItem> items) {
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void addItem(LineItem item) {
        String code = item.getProduct().getCode();
        int quantity = item.getQuantity();

        for (LineItem lineItem : items) {
            if (lineItem.getProduct().getCode().equalsIgnoreCase(code)) {
                lineItem.setQuantity(lineItem.getQuantity() + quantity);
                return;
            }
        }
        items.add(item);
    }

    public void updateQuantity(String code, int quantity) {
        if (quantity <= 0) {
            removeItem(code);
            return;
        }
        for (LineItem item : items) {
            if (item.getProduct().getCode().equalsIgnoreCase(code)) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public void removeItem(String code) {
        for (int i = 0; i < items.size(); i++) {
            LineItem item = items.get(i);
            if (item.getProduct().getCode().equalsIgnoreCase(code)) {
                items.remove(i);
                return;
            }
        }
    }

    public double getTotal() {
        double total = 0.0;
        for (LineItem item : items) {
            total += item.getTotal();
        }
        return total;
    }

    public String getTotalCurrencyFormat() {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        return currency.format(getTotal());
    }
}
