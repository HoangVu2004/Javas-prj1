package AI_PRJ.WEBAPP.controller;

import AI_PRJ.WEBAPP.dto.AddToCartRequest;
import AI_PRJ.WEBAPP.model.Cart;
import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.repository.UserRepository;
import AI_PRJ.WEBAPP.security.UserDetailsImpl;
import AI_PRJ.WEBAPP.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    private User getDemoUser() {
        // For demo purposes, use a default user (e.g., admin with ID 1)
        // In a real application, you would handle unauthenticated users differently
        return userRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Demo user not found in database. Please ensure user with ID 1 exists."));
    }

    @GetMapping
    public ResponseEntity<Cart> getCart() {
        User user = getDemoUser();
        Cart cart = cartService.getCart(user);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestBody AddToCartRequest request) {
        if (request.getProductId() == null || request.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Product ID and quantity must be provided.");
        }
        try {
            User user = getDemoUser();
            cartService.addItemToCart(user, request.getProductId(), request.getQuantity());
            return ResponseEntity.ok().body("Item added to cart successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeItemFromCart(@RequestParam Long kitId) {
        try {
            User user = getDemoUser();
            cartService.removeItemFromCart(user, kitId);
            return ResponseEntity.ok().body("Item removed from cart successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing item from cart.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateItemQuantity(@RequestParam Long kitId, @RequestParam int quantity) {
        try {
            User user = getDemoUser();
            cartService.updateItemQuantityInCart(user, kitId, quantity);
            return ResponseEntity.ok().body("Cart updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating cart.");
        }
    }
}