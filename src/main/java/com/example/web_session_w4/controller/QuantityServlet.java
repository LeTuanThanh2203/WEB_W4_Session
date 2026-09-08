package com.example.web_session_w4.controller;

import com.example.web_session_w4.model.Cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller servlet specifically handling cart item quantity updates and removals (/quantity).
 */
@WebServlet(name = "QuantityServlet", urlPatterns = {"/quantity"})
public class QuantityServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get session and log session ID
        HttpSession session = request.getSession();
        System.out.println("Session ID (QuantityServlet): " + session.getId());

        // 2. Get cart from session
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "update";
        }

        String productId = request.getParameter("productId");
        if (productId == null || productId.trim().isEmpty()) {
            productId = request.getParameter("productCode");
        }

        if (productId != null && !productId.trim().isEmpty()) {
            if ("update".equalsIgnoreCase(action)) {
                String quantityStr = request.getParameter("quantity");
                if (quantityStr != null) {
                    try {
                        int quantity = Integer.parseInt(quantityStr);
                        // 3, 4, 5. Update quantity; if quantity <= 0, remove item from cart
                        cart.updateQuantity(productId, quantity);
                    } catch (NumberFormatException e) {
                        // ignore invalid format
                    }
                }
            } else if ("remove".equalsIgnoreCase(action)) {
                cart.removeItem(productId);
            }
        }

        // Save updated cart back to session
        session.setAttribute("cart", cart);

        // 6. Redirect to cart.jsp
        response.sendRedirect(request.getContextPath() + "/cart.jsp");
    }
}
