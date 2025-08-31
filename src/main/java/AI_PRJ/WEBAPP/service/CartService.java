package AI_PRJ.WEBAPP.service;

import AI_PRJ.WEBAPP.model.Cart;
import AI_PRJ.WEBAPP.model.CartItem;
import AI_PRJ.WEBAPP.model.Kit;
import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.repository.CartItemRepository;
import AI_PRJ.WEBAPP.repository.CartRepository;
import AI_PRJ.WEBAPP.repository.KitRepo;
import AI_PRJ.WEBAPP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private KitRepo kitRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Cart getCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public void addItemToCart(User user, Long productId, int quantity) {
        Cart cart = getCart(user);
        Kit kit = findKitById(productId);

        cart.getItems().stream()
            .filter(item -> item.getKit().getId().equals(productId))
            .findFirst()
            .ifPresentOrElse(
                item -> updateExistingCartItem(item, quantity),
                () -> addNewCartItem(cart, kit, quantity)
            );
    }

    private Kit findKitById(Long productId) {
        return kitRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Kit not found with ID: " + productId));
    }

    private void updateExistingCartItem(CartItem item, int quantity) {
        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);
    }

    private void addNewCartItem(Cart cart, Kit kit, int quantity) {
        CartItem newItem = new CartItem(cart, kit, quantity);
        cart.getItems().add(newItem);
        cartRepository.save(cart);
    }

    @Transactional
    public void removeItemFromCart(User user, Long kitId) {
        Cart cart = getCart(user);
        cart.getItems().removeIf(item -> item.getKit().getId().equals(kitId));
        cartRepository.save(cart);
    }

    @Transactional
    public void updateItemQuantityInCart(User user, Long kitId, int quantity) {
        if (quantity <= 0) {
            removeItemFromCart(user, kitId);
            return;
        }
        Cart cart = getCart(user);
        cart.getItems().stream()
                .filter(item -> item.getKit().getId().equals(kitId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    cartItemRepository.save(item);
                });
    }

    @Transactional
    public void clearCart(User user) {
        Cart cart = getCart(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}