package com.example.web_session_w4.controller;

import com.example.web_session_w4.model.Cart;
import com.example.web_session_w4.model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller servlet to manage shopping cart operations (/cart).
 * Handles adding, updating, removing items, and navigation.
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Default catalog for fallback product information lookup
    private static final Map<String, ProductInfo> CATALOG = new HashMap<>();

    static {
        CATALOG.put("8601", new ProductInfo("86 (the band) - True Life Songs and Pictures", 14.95));
        CATALOG.put("pf01", new ProductInfo("Paddlefoot - The first CD", 12.95));
        CATALOG.put("pf02", new ProductInfo("Paddlefoot - The second CD", 14.95));
        CATALOG.put("jr01", new ProductInfo("Joe Rut - Genuine Wood Grained Finish", 14.95));
    }

    private static class ProductInfo {
        String name;
        double price;

        ProductInfo(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("shop".equalsIgnoreCase(action)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get current user's session (creates a new session if none exists)
        /*
         * EXPLANATION FOR STUDENTS:
         * - Session ID: Tomcat automatically assigns a unique Session ID (JSESSIONID) to each client/browser.
         * - Session storage: HttpSession stores data on the server side tied to this specific client's Session ID.
         * - Lifetime: The cart persists across requests as long as the session is active.
         * - Expiration: When the session expires (or user closes browser/logs out), Tomcat invalidates the session and the cart is lost.
         */
        HttpSession session = request.getSession();
        System.out.println("Session ID: " + session.getId());

        // 2. Retrieve existing cart from session, or create a new one if it doesn't exist yet
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }

        // 3. Determine requested action (default: view cart)
        String action = request.getParameter("action");
        if (action == null) {
            action = "cart";
        }

        String redirectUrl = request.getContextPath() + "/cart.jsp";

        // 4. Handle actions
        if ("add".equalsIgnoreCase(action)) {
            // Read product parameter (supports both 'productId' and 'productCode')
            String productId = request.getParameter("productId");
            if (productId == null || productId.trim().isEmpty()) {
                productId = request.getParameter("productCode");
            }

            String productName = request.getParameter("productName");
            if (productName == null || productName.trim().isEmpty()) {
                productName = request.getParameter("description");
            }

            String priceStr = request.getParameter("price");
            String quantityStr = request.getParameter("quantity");

            int quantity = 1;
            if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                try {
                    quantity = Integer.parseInt(quantityStr);
                } catch (NumberFormatException e) {
                    quantity = 1;
                }
            }

            double price = 0.0;
            if (priceStr != null && !priceStr.trim().isEmpty()) {
                try {
                    price = Double.parseDouble(priceStr.replace("$", ""));
                } catch (NumberFormatException e) {
                    price = 0.0;
                }
            }

            // Fallback lookup from catalog if name/price were not provided in form inputs
            if (productId != null && (productName == null || price <= 0)) {
                ProductInfo info = CATALOG.get(productId);
                if (info != null) {
                    if (productName == null || productName.trim().isEmpty()) {
                        productName = info.name;
                    }
                    if (price <= 0) {
                        price = info.price;
                    }
                }
            }

            if (productId != null && !productId.trim().isEmpty()) {
                CartItem item = new CartItem(productId, productName, price, quantity);
                // Add item or increase quantity if product already in cart
                cart.addItem(item);
            }

            // Save updated cart back to session attribute "cart"
            session.setAttribute("cart", cart);
            redirectUrl = request.getContextPath() + "/cart.jsp";

        } else if ("update".equalsIgnoreCase(action)) {
            String productId = request.getParameter("productId");
            if (productId == null) {
                productId = request.getParameter("productCode");
            }

            String quantityStr = request.getParameter("quantity");
            if (productId != null && quantityStr != null) {
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    cart.updateQuantity(productId, quantity);
                } catch (NumberFormatException e) {
                    // ignore invalid input
                }
            }
            session.setAttribute("cart", cart);
            redirectUrl = request.getContextPath() + "/cart.jsp";

        } else if ("remove".equalsIgnoreCase(action)) {
            String productId = request.getParameter("productId");
            if (productId == null) {
                productId = request.getParameter("productCode");
            }

            if (productId != null) {
                cart.removeItem(productId);
            }
            session.setAttribute("cart", cart);
            redirectUrl = request.getContextPath() + "/cart.jsp";

        } else if ("shop".equalsIgnoreCase(action)) {
            redirectUrl = request.getContextPath() + "/index.jsp";

        } else if ("checkout".equalsIgnoreCase(action)) {
            redirectUrl = request.getContextPath() + "/cart.jsp";
        }

        // Post-Redirect-Get pattern to avoid double form submissions on refresh
        response.sendRedirect(redirectUrl);
    }
}
