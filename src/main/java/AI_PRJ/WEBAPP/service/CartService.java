package AI_PRJ.WEBAPP.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import AI_PRJ.WEBAPP.model.Cart;
import AI_PRJ.WEBAPP.model.CartItem;
import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.repository.CartItemRepository;
import AI_PRJ.WEBAPP.repository.CartRepository;
import AI_PRJ.WEBAPP.repository.KitRepo;
import AI_PRJ.WEBAPP.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KitRepo kitRepository; // To validate kit existence and get price

    @Transactional
    public Cart getOrCreateCartForUser(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
                    Cart newCart = new Cart(userId);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public Cart addItemToCart(Long userId, Long kitId, int quantity) {
        if (kitId == null) {
            throw new IllegalArgumentException("Kit ID cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        // Validate Kit existence
        kitRepository.findById(kitId)
                .orElseThrow(() -> new RuntimeException("Kit not found with ID: " + kitId));

        Cart cart = getOrCreateCartForUser(userId);
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getKitId().equals(kitId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(kitId, quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }
        return cart;
    }

    @Transactional
    public Cart removeItemFromCart(Long userId, Long kitId) {
        Cart cart = getOrCreateCartForUser(userId);
        cart.getItems().removeIf(item -> {
            if (item.getKitId().equals(kitId)) {
                cartItemRepository.delete(item);
                return true;
            }
            return false;
        });
        return cart;
    }

    @Transactional
    public Cart updateItemQuantityInCart(Long userId, Long kitId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        Cart cart = getOrCreateCartForUser(userId);
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getKitId().equals(kitId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            if (quantity == 0) {
                cart.getItems().remove(item);
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        } else {
            throw new RuntimeException("Cart item not found for Kit ID: " + kitId);
        }
        return cart;
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCartForUser(userId);
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
    }
}