package AI_PRJ.WEBAPP.service;

import AI_PRJ.WEBAPP.model.*;
import AI_PRJ.WEBAPP.repository.CartRepository;
import AI_PRJ.WEBAPP.repository.OrderRepository;
import AI_PRJ.WEBAPP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Order createOrderFromCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user with ID: " + userId));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot create an order from an empty cart.");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setKit(cartItem.getKit());
            orderItem.setQuantity(cartItem.getQuantity());
            BigDecimal priceAtPurchase = BigDecimal.valueOf(cartItem.getKit().getPrice());
            orderItem.setPriceAtPurchase(priceAtPurchase);
            order.addOrderItem(orderItem);

            totalAmount = totalAmount.add(priceAtPurchase.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        // Clear the cart after creating the order
        cart.getItems().clear();
        cartRepository.save(cart);

        return order;
    }

    public List<Order> getOrdersForUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}