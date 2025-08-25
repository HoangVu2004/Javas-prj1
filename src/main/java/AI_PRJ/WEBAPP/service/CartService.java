package AI_PRJ.WEBAPP.service;

import AI_PRJ.WEBAPP.model.Cart;
import AI_PRJ.WEBAPP.model.CartItem;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession; // This import should now be resolved

@Service
public class CartService {

    private static final String CART_SESSION_ATTRIBUTE = "userCart";

    // Method to get the cart from the session
    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart(); // Assuming Cart has a default constructor
            session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
        return cart;
    }

    public void addItemToCart(HttpSession session, Long kitId, int quantity) {
        Cart cart = getCart(session);
        // Ensure kitId is not null before proceeding
        if (kitId == null) {
            // Throw an exception if kitId is null, to be handled by the controller
            throw new IllegalArgumentException("Kit ID cannot be null.");
        }
        CartItem newItem = new CartItem(kitId, quantity);
        cart.addItem(newItem);
        session.setAttribute(CART_SESSION_ATTRIBUTE, cart); // Explicitly set attribute
        // Session is updated automatically when the attribute is modified if it's mutable
        // If Cart was immutable, you'd need to re-set it: session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
    }

    public void removeItemFromCart(HttpSession session, Long kitId) {
        Cart cart = getCart(session);
        cart.removeItem(kitId);
        session.setAttribute(CART_SESSION_ATTRIBUTE, cart); // Explicitly set attribute
        // Session update might be needed if Cart is immutable
    }

    public void updateItemQuantityInCart(HttpSession session, Long kitId, int quantity) {
        Cart cart = getCart(session);
        cart.updateItemQuantity(kitId, quantity);
        session.setAttribute(CART_SESSION_ATTRIBUTE, cart); // Explicitly set attribute
        // Session update might be needed if Cart is immutable
    }

    // Method to clear the cart
    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_ATTRIBUTE);
    }

    // Placeholder for calculating total price.
    // For a simple cart, we might just sum quantities or use hardcoded prices if product details aren't readily available.
    // public double calculateCartTotal(HttpSession session) {
    //     Cart cart = getCart(session);
    //     double total = 0;
    //     for (CartItem item : cart.getItems()) {
    //         // In a real app, you'd fetch the price for item.getKitId() here.
    //         // For simplicity, let's just sum quantities as a placeholder for calculation.
    //         total += item.getQuantity(); // Placeholder: sum of quantities
    //     }
    //     return total;
    // }
}