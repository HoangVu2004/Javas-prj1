package AI_PRJ.WEBAPP.controller;

import AI_PRJ.WEBAPP.dto.AddToCartRequest;
import AI_PRJ.WEBAPP.model.Cart;
import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.service.CartService;
import AI_PRJ.WEBAPP.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.findByUsername(userDetails.getUsername());
    }

    @GetMapping
    public ResponseEntity<Cart> getCart() {
        User user = getCurrentUser();
        Cart cart = cartService.getCart(user);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestBody AddToCartRequest request) {
        if (request.getProductId() == null || request.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Product ID and quantity must be provided.");
        }
        try {
            User user = getCurrentUser();
            cartService.addItemToCart(user, request.getProductId(), request.getQuantity());
            return ResponseEntity.ok().body("Item added to cart successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeItemFromCart(@RequestParam Long kitId) {
        try {
            User user = getCurrentUser();
            cartService.removeItemFromCart(user, kitId);
            return ResponseEntity.ok().body("Item removed from cart successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing item from cart.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateItemQuantity(@RequestParam Long kitId, @RequestParam int quantity) {
        if (quantity <= 0) {
            // Let the service handle the logic of removing the item if quantity is 0 or less
        }
        try {
            User user = getCurrentUser();
            cartService.updateItemQuantityInCart(user, kitId, quantity);
            return ResponseEntity.ok().body("Cart updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating cart.");
        }
    }
}