package com.example.web_session_w4.controller;

import com.example.web_session_w4.model.Cart;
import com.example.web_session_w4.model.LineItem;
import com.example.web_session_w4.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart", "/quantity"})
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "cart";
        }

        String url = "/cart.jsp";

        switch (action.toLowerCase().trim()) {
            case "shop":
                url = "/index.jsp";
                break;

            case "add":
                addToCart(request, cart);
                session.setAttribute("cart", cart);
                url = "/cart.jsp";
                break;

            case "update":
                updateCart(request, cart);
                session.setAttribute("cart", cart);
                url = "/cart.jsp";
                break;

            case "remove":
                removeFromCart(request, cart);
                session.setAttribute("cart", cart);
                url = "/cart.jsp";
                break;

            case "checkout":
            case "cart":
            default:
                url = "/cart.jsp";
                break;
        }

        getServletContext().getRequestDispatcher(url).forward(request, response);
    }

    private void addToCart(HttpServletRequest request, Cart cart) {
        String productCode = getParam(request, "productCode", "productId");
        String productName = getParam(request, "productName", "description");
        double price = parseDouble(request.getParameter("price"));
        int quantity = parseInt(request.getParameter("quantity"), 1);

        Product product = new Product(productCode, productName, price);
        cart.addItem(new LineItem(product, quantity));
    }

    private void updateCart(HttpServletRequest request, Cart cart) {
        String productCode = getParam(request, "productCode", "productId");
        int quantity = parseInt(request.getParameter("quantity"), 0);

        if (quantity > 0) {
            cart.updateQuantity(productCode, quantity);
        } else {
            cart.removeItem(productCode);
        }
    }

    private void removeFromCart(HttpServletRequest request, Cart cart) {
        String productCode = getParam(request, "productCode", "productId");
        cart.removeItem(productCode);
    }

    private String getParam(HttpServletRequest request, String mainParam, String fallbackParam) {
        String val = request.getParameter(mainParam);
        if (val == null || val.trim().isEmpty()) {
            val = request.getParameter(fallbackParam);
        }
        return val != null ? val.trim() : "";
    }

    private int parseInt(String val, int defaultVal) {
        try {
            return (val != null && !val.trim().isEmpty()) ? Integer.parseInt(val.trim()) : defaultVal;
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private double parseDouble(String val) {
        try {
            return (val != null && !val.trim().isEmpty()) ? Double.parseDouble(val.replace("$", "").trim()) : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}
